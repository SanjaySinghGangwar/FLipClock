package sanjaygangwar.dev.flipclocklibraray

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import sanjaygangwar.dev.flipclockLibrary.R

import java.util.concurrent.TimeUnit

class CountDownClock : LinearLayout {


    private var countDownTimer: CountDownTimer? = null
    private var countdownListener: CountdownCallBack? = null
    private var countdownTickInterval = 1000

    private var almostFinishedCallbackTimeInSeconds: Int = 5

    private var resetSymbol: String = "8"

    var firstDigitMinute = findViewById<CountDownDigit>(R.id.firstDigitMinute)
    var secondDigitMinute = findViewById<CountDownDigit>(R.id.secondDigitMinute)
    var firstDigitSecond = findViewById<CountDownDigit>(R.id.firstDigitSecond)
    var secondDigitSecond = findViewById<CountDownDigit>(R.id.secondDigitSecond)
    var digitsSplitter = findViewById<TextView>(R.id.digitsSplitter)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        View.inflate(context, R.layout.view_simple_clock, this)
        firstDigitMinute = findViewById(R.id.firstDigitMinute)
        firstDigitMinute = findViewById(R.id.firstDigitMinute)
        secondDigitMinute = findViewById(R.id.secondDigitMinute)
        firstDigitSecond = findViewById(R.id.firstDigitSecond)
        secondDigitSecond = findViewById(R.id.secondDigitSecond)
        digitsSplitter = findViewById(R.id.digitsSplitter)

        attrs?.let {
            val typedArray =
                context?.obtainStyledAttributes(attrs, R.styleable.CountDownClock, defStyleAttr, 0)

            val resetSymbol = typedArray?.getString(R.styleable.CountDownClock_resetSymbol)
            if (resetSymbol != null) {
                setResetSymbol(resetSymbol)
            }

            val digitTopDrawable =
                typedArray?.getDrawable(R.styleable.CountDownClock_digitTopDrawable)
            setDigitTopDrawable(digitTopDrawable)
            val digitBottomDrawable =
                typedArray?.getDrawable(R.styleable.CountDownClock_digitBottomDrawable)
            setDigitBottomDrawable(digitBottomDrawable)
            val digitDividerColor =
                typedArray?.getColor(R.styleable.CountDownClock_digitDividerColor, 0)
            setDigitDividerColor(digitDividerColor ?: 0)
            val digitSplitterColor =
                typedArray?.getColor(R.styleable.CountDownClock_digitSplitterColor, 0)
            setDigitSplitterColor(digitSplitterColor ?: 0)

            val digitTextColor = typedArray?.getColor(R.styleable.CountDownClock_digitTextColor, 0)
            setDigitTextColor(digitTextColor ?: 0)

            val digitTextSize =
                typedArray?.getDimension(R.styleable.CountDownClock_digitTextSize, 0f)
            setDigitTextSize(digitTextSize ?: 0f)
            setSplitterDigitTextSize(digitTextSize ?: 0f)

            val digitPadding = typedArray?.getDimension(R.styleable.CountDownClock_digitPadding, 0f)
            setDigitPadding(digitPadding?.toInt() ?: 0)

            val splitterPadding =
                typedArray?.getDimension(R.styleable.CountDownClock_splitterPadding, 0f)
            setSplitterPadding(splitterPadding?.toInt() ?: 0)

            val halfDigitHeight =
                typedArray?.getDimensionPixelSize(R.styleable.CountDownClock_halfDigitHeight, 0)
            val digitWidth =
                typedArray?.getDimensionPixelSize(R.styleable.CountDownClock_digitWidth, 0)
            setHalfDigitHeightAndDigitWidth(halfDigitHeight ?: 0, digitWidth ?: 0)

            val animationDuration =
                typedArray?.getInt(R.styleable.CountDownClock_animationDuration, 0)
            setAnimationDuration(animationDuration ?: 600)

            val almostFinishedCallbackTimeInSeconds = typedArray?.getInt(
                R.styleable.CountDownClock_almostFinishedCallbackTimeInSeconds,
                5
            )
            setAlmostFinishedCallbackTimeInSeconds(almostFinishedCallbackTimeInSeconds ?: 5)

            val countdownTickInterval =
                typedArray?.getInt(R.styleable.CountDownClock_countdownTickInterval, 1000)
            this.countdownTickInterval = countdownTickInterval ?: 1000

            invalidate()
            typedArray?.recycle()
        }
    }

    ////////////////
    // Public methods
    ////////////////

    fun startCountDown(timeToNextEvent: Long) {
        countDownTimer?.cancel()
        var hasCalledAlmostFinished = false
        countDownTimer = object : CountDownTimer(timeToNextEvent, countdownTickInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 <= almostFinishedCallbackTimeInSeconds && !hasCalledAlmostFinished) {
                    hasCalledAlmostFinished = true
                    countdownListener?.countdownAboutToFinish()
                }
                setCountDownTime(millisUntilFinished)
            }

            override fun onFinish() {
                hasCalledAlmostFinished = false
                countdownListener?.countdownFinished()
            }
        }
        countDownTimer?.start()
    }

    fun resetCountdownTimer() {
        countDownTimer?.cancel()
        firstDigitMinute.setNewText(resetSymbol)
        secondDigitMinute.setNewText(resetSymbol)
        firstDigitSecond.setNewText(resetSymbol)
        secondDigitSecond.setNewText(resetSymbol)
    }

    ////////////////
    // Private methods
    ////////////////

    private fun setCountDownTime(timeToStart: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeToStart)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(timeToStart - TimeUnit.MINUTES.toMillis(minutes))

        val minutesString = minutes.toString()
        val secondsString = seconds.toString()
        if (minutesString.length == 2) {
            firstDigitMinute.animateTextChange(minutesString[0].toString())
            secondDigitMinute.animateTextChange(minutesString[1].toString())
        } else if (minutesString.length == 1) {
            firstDigitMinute.animateTextChange("0")
            secondDigitMinute.animateTextChange(minutesString[0].toString())
        } else {
            firstDigitMinute.animateTextChange("5")
            secondDigitMinute.animateTextChange("9")
        }
        if (secondsString.length == 2) {
            firstDigitSecond.animateTextChange(secondsString[0].toString())
            secondDigitSecond.animateTextChange(secondsString[1].toString())
        } else if (secondsString.length == 1) {
            firstDigitSecond.animateTextChange("0")
            secondDigitSecond.animateTextChange(secondsString[0].toString())
        } else {
            firstDigitSecond.animateTextChange(secondsString[secondsString.length - 2].toString())
            secondDigitSecond.animateTextChange(secondsString[secondsString.length - 1].toString())
        }
    }

    private fun setResetSymbol(resetSymbol: String?) {
        resetSymbol?.let {
            if (it.isNotEmpty()) {
                this.resetSymbol = resetSymbol
            } else {
                this.resetSymbol = ""
            }
        } ?: kotlin.run {
            this.resetSymbol = ""
        }
    }

    private fun setDigitTopDrawable(digitTopDrawable: Drawable?) {
        if (digitTopDrawable != null) {
            firstDigitMinute.frontUpper.background = digitTopDrawable
            firstDigitMinute.backUpper.background = digitTopDrawable
            secondDigitMinute.frontUpper.background = digitTopDrawable
            secondDigitMinute.backUpper.background = digitTopDrawable
            firstDigitSecond.frontUpper.background = digitTopDrawable
            firstDigitSecond.backUpper.background = digitTopDrawable
            secondDigitSecond.frontUpper.background = digitTopDrawable
            secondDigitSecond.backUpper.background = digitTopDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitBottomDrawable(digitBottomDrawable: Drawable?) {
        if (digitBottomDrawable != null) {
            firstDigitMinute.frontLower.background = digitBottomDrawable
            firstDigitMinute.backLower.background = digitBottomDrawable
            secondDigitMinute.frontLower.background = digitBottomDrawable
            secondDigitMinute.backLower.background = digitBottomDrawable
            firstDigitSecond.frontLower.background = digitBottomDrawable
            firstDigitSecond.backLower.background = digitBottomDrawable
            secondDigitSecond.frontLower.background = digitBottomDrawable
            secondDigitSecond.backLower.background = digitBottomDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitDividerColor(digitDividerColor: Int) {
        var dividerColor = digitDividerColor
        if (dividerColor == 0) {
            dividerColor = context.resources.getColor(R.color.transparent)
        }
        firstDigitMinute.digitDivider.setBackgroundColor(dividerColor)
        secondDigitMinute.digitDivider.setBackgroundColor(dividerColor)
        firstDigitSecond.digitDivider.setBackgroundColor(dividerColor)
        secondDigitSecond.digitDivider.setBackgroundColor(dividerColor)
    }

    private fun setDigitSplitterColor(digitsSplitterColor: Int) {
        if (digitsSplitterColor != 0) {
            digitsSplitter.setTextColor(digitsSplitterColor)
        } else {
            digitsSplitter.setTextColor(context.resources.getColor(R.color.transparent))
        }
    }

    private fun setSplitterDigitTextSize(digitsTextSize: Float) {
        digitsSplitter.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
    }

    private fun setDigitPadding(digitPadding: Int) {
        firstDigitMinute.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        secondDigitMinute.setPadding(digitPadding, digitPadding, 0, digitPadding)
        firstDigitSecond.setPadding(0, digitPadding, digitPadding, digitPadding)
        secondDigitSecond.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
    }

    private fun setSplitterPadding(splitterPadding: Int) {
        digitsSplitter.setPadding(splitterPadding, 0, splitterPadding, 0)
    }

    private fun setDigitTextColor(digitsTextColor: Int) {
        var textColor = digitsTextColor
        if (textColor == 0) {
            textColor = context.resources.getColor(R.color.transparent)
        }
        firstDigitMinute.frontUpperText.setTextColor(textColor)
        firstDigitMinute.backUpperText.setTextColor(textColor)
        secondDigitMinute.frontUpperText.setTextColor(textColor)
        secondDigitMinute.backUpperText.setTextColor(textColor)
        firstDigitSecond.frontUpperText.setTextColor(textColor)
        firstDigitSecond.backUpperText.setTextColor(textColor)
        secondDigitSecond.frontUpperText.setTextColor(textColor)
        secondDigitSecond.backUpperText.setTextColor(textColor)

        firstDigitMinute.frontLowerText.setTextColor(textColor)
        firstDigitMinute.backLowerText.setTextColor(textColor)
        secondDigitMinute.frontLowerText.setTextColor(textColor)
        secondDigitMinute.backLowerText.setTextColor(textColor)
        firstDigitSecond.frontLowerText.setTextColor(textColor)
        firstDigitSecond.backLowerText.setTextColor(textColor)
        secondDigitSecond.frontLowerText.setTextColor(textColor)
        secondDigitSecond.backLowerText.setTextColor(textColor)
    }

    private fun setDigitTextSize(digitsTextSize: Float) {
        firstDigitMinute.frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitMinute.backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitMinute.frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitMinute.backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitSecond.frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitSecond.backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitSecond.frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitSecond.backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitMinute.frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitMinute.backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitMinute.frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitMinute.backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitSecond.frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        firstDigitSecond.backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitSecond.frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        secondDigitSecond.backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
    }

    private fun setHalfDigitHeightAndDigitWidth(halfDigitHeight: Int, digitWidth: Int) {
        setHeightAndWidthToView(firstDigitMinute.frontUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(firstDigitMinute.backUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitMinute.frontUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitMinute.backUpper, halfDigitHeight, digitWidth)

        setHeightAndWidthToView(firstDigitSecond.frontUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(firstDigitSecond.backUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitSecond.frontUpper, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitSecond.backUpper, halfDigitHeight, digitWidth)

        // Lower
        setHeightAndWidthToView(firstDigitMinute.frontLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(firstDigitMinute.backLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitMinute.frontLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitMinute.backLower, halfDigitHeight, digitWidth)

        setHeightAndWidthToView(firstDigitSecond.frontLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(firstDigitSecond.backLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitSecond.frontLower, halfDigitHeight, digitWidth)
        setHeightAndWidthToView(secondDigitSecond.backLower, halfDigitHeight, digitWidth)

        // Dividers
        firstDigitMinute.digitDivider.layoutParams.width = digitWidth
        secondDigitMinute.digitDivider.layoutParams.width = digitWidth
        firstDigitSecond.digitDivider.layoutParams.width = digitWidth
        secondDigitSecond.digitDivider.layoutParams.width = digitWidth
    }

    private fun setHeightAndWidthToView(view: View, halfDigitHeight: Int, digitWidth: Int) {
        val firstDigitMinuteFrontUpperLayoutParams = view.layoutParams
        firstDigitMinuteFrontUpperLayoutParams.height = halfDigitHeight
        firstDigitMinuteFrontUpperLayoutParams.width = digitWidth
        firstDigitMinute.frontUpper.layoutParams = firstDigitMinuteFrontUpperLayoutParams
    }

    private fun setAnimationDuration(animationDuration: Int) {
        firstDigitMinute.setAnimationDuration(animationDuration.toLong())
        secondDigitMinute.setAnimationDuration(animationDuration.toLong())
        firstDigitSecond.setAnimationDuration(animationDuration.toLong())
        secondDigitSecond.setAnimationDuration(animationDuration.toLong())
    }

    private fun setAlmostFinishedCallbackTimeInSeconds(almostFinishedCallbackTimeInSeconds: Int) {
        this.almostFinishedCallbackTimeInSeconds = almostFinishedCallbackTimeInSeconds
    }

    private fun setTransparentBackgroundColor() {
        val transparent = context.resources.getColor(R.color.transparent)
        firstDigitMinute.frontLower.setBackgroundColor(transparent)
        firstDigitMinute.backLower.setBackgroundColor(transparent)
        secondDigitMinute.frontLower.setBackgroundColor(transparent)
        secondDigitMinute.backLower.setBackgroundColor(transparent)
        firstDigitSecond.frontLower.setBackgroundColor(transparent)
        firstDigitSecond.backLower.setBackgroundColor(transparent)
        secondDigitSecond.frontLower.setBackgroundColor(transparent)
        secondDigitSecond.backLower.setBackgroundColor(transparent)
    }

    ////////////////
    // Listeners
    ////////////////

    public fun setCountdownListener(countdownListener: CountdownCallBack) {
        this.countdownListener = countdownListener
    }

    public interface CountdownCallBack {
        fun countdownAboutToFinish()
        fun countdownFinished()
    }
}