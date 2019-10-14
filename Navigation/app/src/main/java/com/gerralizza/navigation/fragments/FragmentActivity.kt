package com.gerralizza.navigation.fragments

import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.gerralizza.navigation.R


// Пример насильно усложнен тем, что в портретной ориентации мы открываем фуллскрин-картинку
// прям в этой активити в основном контайнере (id=fragment_container)
// Поэтому нам нужно самим раскидать фрагменты по нужным контейнерам при поворотах
// В простом флоу вы скорее всего будете использовать запуск новой активити для фуллскрина
// (но это не точно)
class FragmentActivity : AppCompatActivity(), ImagePathClickListener {
    private var fragmentExtraContainer: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_activity)

        fragmentExtraContainer = findViewById(R.id.fragment_extra_container)

        val upEnabled = fragmentExtraContainer == null
        supportActionBar?.setDisplayShowHomeEnabled(upEnabled)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)

        if (savedInstanceState == null) { // First launch
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ImagesListFragment(), TAG_LIST)
                .commit()
        } else if (fragmentExtraContainer != null) { // Portrait -> Landscape
            val imageFragment = supportFragmentManager.findFragmentByTag(TAG_FULLSCREEN)

            if (imageFragment != null) { // Мы открывали фулскрин и у нас есть транзакция в бэкстеке
                supportFragmentManager.popBackStack(
                    FULLSCREEN_BACK_STACK,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                supportFragmentManager.executePendingTransactions()

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_extra_container, imageFragment, TAG_FULLSCREEN)
                    .commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onImageClick(path: String, root: View, image: ImageView) {
        val imageFragment = FullScreenImageFragment.create(path)

        if (fragmentExtraContainer != null) {
            imageFragment.enterTransition = Fade().apply { duration = 500L }
            imageFragment.exitTransition = Fade().apply { duration = 500L }

            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_extra_container,
                    imageFragment,
                    TAG_FULLSCREEN
                )
                .commit()
        } else {
            val translation = TransitionInflater
                .from(this)
                .inflateTransition(R.transition.shared_image_transition)

            supportFragmentManager.findFragmentByTag(TAG_LIST)?.let {
                it.exitTransition = Fade().apply {
                    duration = translation.duration
                    interpolator = translation.interpolator
                    addTarget(CardView::class.java)
                    excludeTarget(root, true)
                    addTarget(root.findViewById<View>(R.id.title))
                }
            }

            imageFragment.sharedElementEnterTransition = translation

            supportFragmentManager
                .beginTransaction()
                .addSharedElement(image, getString(R.string.image_transition))
                .replace(
                    R.id.fragment_container,
                    imageFragment,
                    TAG_FULLSCREEN
                )
                .addToBackStack(FULLSCREEN_BACK_STACK)
                .commit()
        }
    }

    companion object {
        private const val TAG_LIST = "list"
        private const val TAG_FULLSCREEN = "fullscreen"
        private const val FULLSCREEN_BACK_STACK = "fullscreen_back_stack"
    }
}