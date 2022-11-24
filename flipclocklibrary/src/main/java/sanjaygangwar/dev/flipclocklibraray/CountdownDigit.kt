package sanjaygangwar.dev.flipclocklibraray

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import sanjaygangwar.dev.flipclockLibrary.R

class CountDownDigit : FrameLayout {
    private var animationDuration = 600L
    var frontUpperText = findViewById<AlignedTextView>(R.id.frontUpperText)
    var frontLowerText = findViewById<AlignedTextView>(R.id.frontLowerText)
    var backUpperText = findViewById<AlignedTextView>(R.id.backUpperText)
    var backLowerText = findViewById<AlignedTextView>(R.id.backLowerText)
    var frontUpper = findViewById<FrameLayout>(R.id.frontUpper)
    var frontLower = findViewById<FrameLayout>(R.id.frontLower)
    var digitDivider = findViewById<View>(R.id.digitDivider)
    var backLower = findViewById<FrameLayout>(R.id.backLower)
    var backUpper = findViewById<FrameLayout>(R.id.backUpper)

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.view_countdown_clock_digit, this)

        frontUpperText = findViewById<AlignedTextView>(R.id.frontUpperText)
        frontLowerText = findViewById<AlignedTextView>(R.id.frontLowerText)
        backUpperText = findViewById<AlignedTextView>(R.id.backUpperText)
        backLowerText = findViewById<AlignedTextView>(R.id.backLowerText)
        frontUpper = findViewById<FrameLayout>(R.id.frontUpper)
        frontLower = findViewById<FrameLayout>(R.id.frontLower)
        digitDivider = findViewById<View>(R.id.digitDivider)
        backLower = findViewById<FrameLayout>(R.id.backLower)
        backUpper = findViewById<FrameLayout>(R.id.backUpper)

        frontUpperText.measure(0, 0)
        frontLowerText.measure(0, 0)
        backUpperText.measure(0, 0)
        backLowerText.measure(0, 0)
    }

    fun setNewText(newText: String) {
        frontUpper.clearAnimation()
        frontLower.clearAnimation()

        frontUpperText.text = newText
        frontLowerText.text = newText
        backUpperText.text = newText
        backLowerText.text = newText
    }

    fun animateTextChange(newText: String) {
        if (backUpperText.text == newText) {
            return
        }

        frontUpper.clearAnimation()
        frontLower.clearAnimation()

        backUpperText.text = newText
        frontUpper.pivotY = frontUpper.bottom.toFloat()
        frontLower.pivotY = frontUpper.top.toFloat()
        frontUpper.pivotX =
            (frontUpper.right - ((frontUpper.right - frontUpper.left) / 2)).toFloat()
        frontLower.pivotX =
            (frontUpper.right - ((frontUpper.right - frontUpper.left) / 2)).toFloat()

        frontUpper.animate().setDuration(getHalfOfAnimationDuration()).rotationX(-90f)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction({
                frontUpperText.text = backUpperText.text
                frontUpper.rotationX = 0f

                frontLower.rotationX = 90f
                frontLowerText.text = backUpperText.text
                frontLower.animate().setDuration(getHalfOfAnimationDuration()).rotationX(0f)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction({
                        backLowerText.text = frontLowerText.text
                    }).start()
            }).start()
    }

    fun setAnimationDuration(duration: Long) {
        this.animationDuration = duration
    }

    private fun getHalfOfAnimationDuration(): Long {
        return animationDuration / 2
    }
}