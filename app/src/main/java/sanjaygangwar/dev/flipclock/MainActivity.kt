package sanjaygangwar.dev.flipclock

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import sanjaygangwar.dev.flipclock.databinding.ActivityMainBinding
import sanjaygangwar.dev.flipclocklibraray.CountDownClock

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
    }

    override fun onResume() {
        super.onResume()

        bind.countdownClockFirst.setCountdownListener(object : CountDownClock.CountdownCallBack {
            override fun countdownAboutToFinish() {
                Log.d("here", "Countdown first is about to finish")
            }

            override fun countdownFinished() {
                Log.d("here", "Countdown first finished")
                bind.countdownClockFirst.startCountDown(15000)
            }
        })

        bind.countdownClockFirst.startCountDown(15000)

        bind.countdownClockSecond.setCountdownListener(object : CountDownClock.CountdownCallBack {
            override fun countdownAboutToFinish() {
                Log.d("here", "Countdown second is about to finish")
            }

            override fun countdownFinished() {
                Log.d("here", "Countdown second finished")
                bind.countdownClockSecond.startCountDown(70000)
            }
        })

        bind.countdownClockSecond.startCountDown(70000)

        bind.countdownClockThird.setCountdownListener(object : CountDownClock.CountdownCallBack {
            override fun countdownAboutToFinish() {
                Log.d("here", "Countdown third is about to finish")
            }

            override fun countdownFinished() {
                Log.d("here", "Countdown third finished")
                bind.countdownClockThird.startCountDown(570000)
            }
        })

        bind.countdownClockThird.startCountDown(570000)
    }

    override fun onPause() {
        super.onPause()

        bind.countdownClockFirst.resetCountdownTimer()
        bind.countdownClockSecond.resetCountdownTimer()
        bind.countdownClockThird.resetCountdownTimer()
    }
}