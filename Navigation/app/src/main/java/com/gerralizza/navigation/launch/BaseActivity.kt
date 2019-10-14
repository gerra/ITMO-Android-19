package com.gerralizza.navigation.launch

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.gerralizza.navigation.R

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = javaClass.simpleName

        tag = javaClass.simpleName + "($taskId)" + "#${hashCode()}"

        Log.d(tag, "onCreate")

        setContentView(R.layout.activity_launches)

        findViewById<ViewGroup>(R.id.root).forEach { child ->
            child.setOnClickListener { onClick(it) }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(tag, "onResume()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d(tag, "onNewIntent()")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(tag, "onDestroy()")
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        overridePendingTransition(0, 0)

//        super.startActivity(intent)
//        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)

//        super.finish()
//        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.start_a_standard -> startActivity(Intent(this, ActivityA::class.java))
            R.id.start_b_standard -> startActivity(Intent(this, ActivityB::class.java))
            R.id.start_a_stop -> startActivity(Intent(this, ActivityA::class.java).singleTop())
            R.id.start_b_stop -> startActivity(Intent(this, ActivityB::class.java).singleTop())
            R.id.start_a_nt -> startActivity(Intent(this, ActivityA::class.java).newTask(true))
            R.id.start_b_nt -> startActivity(Intent(this, ActivityB::class.java).newTask(false))
            R.id.start_a_ctop -> startActivity(Intent(this, ActivityA::class.java).clearTop())
            R.id.start_b_ctop -> startActivity(Intent(this, ActivityB::class.java).clearTop())
            R.id.start_a_ctask -> startActivity(Intent(this, ActivityA::class.java).clearTask())
            R.id.start_b_ctask -> startActivity(Intent(this, ActivityB::class.java).clearTask())
            R.id.start_c -> startActivity(Intent(this, ActivityC::class.java))
            R.id.start_d -> startActivity(Intent(this, ActivityD::class.java))
        }
    }

    private fun Intent.singleTop(): Intent {
        return addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    private fun Intent.newTask(multipleTask: Boolean): Intent {
        return addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).apply {
            if (multipleTask) {
                addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
        }
    }

    private fun Intent.clearTop(): Intent {
        //!
        return addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    private fun Intent.clearTask(): Intent {
        return addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}