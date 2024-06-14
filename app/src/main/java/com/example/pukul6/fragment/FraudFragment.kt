package com.example.pukul6.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pukul6.R
import com.example.pukul6.data.database.ClassificationResult
import com.example.pukul6.viewmodel.FraudViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class FraudFragment : Fragment() {
    private lateinit var fraudViewModel: FraudViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fraud, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = "2837b4e5c8ce7a7be2d39ada35aaa334"

        fraudViewModel = ViewModelProvider(this).get(FraudViewModel::class.java)
        progressBar = view.findViewById(R.id.progressBar)

        val inputText = view.findViewById<EditText>(R.id.inputText)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val inputValue = inputText.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("text", inputValue)
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            fraudViewModel.getProb(requestBody,token)
        }

        fraudViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        fraudViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        fraudViewModel.classificationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                showResultDialog(it)
            }
        }
    }

    private fun showResultDialog(result: ClassificationResult) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_result, null)
        val tvResultValue = dialogView.findViewById<TextView>(R.id.tv_item_label)
        val tvResultText = dialogView.findViewById<TextView>(R.id.tv_item_text)
        val tvResultBias = dialogView.findViewById<TextView>(R.id.tv_item_bias)
        val tvResultList = dialogView.findViewById<TextView>(R.id.listEntitites)

        val probability = result.probability
        val label = if (probability > 0.5) "Hoax" else "Valid"
        tvResultValue.text = label
        tvResultText.text = result.inputText
        tvResultBias.text = result.bias[0].toString()

        val bulletPoint = "\u2022" // Unicode for bullet point
        val formattedEntities = result.entities.joinToString("\n") { "$bulletPoint $it" }
        tvResultList.text = formattedEntities


      tvResultText.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
           override fun onGlobalLayout() {
               tvResultText.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setupExpandableText(tvResultText, result.inputText)
            }
       })

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun setupExpandableText(textView: TextView, text: String) {
        val maxLines = 3
        textView.maxLines = maxLines

        textView.post {
            if (textView.lineCount > maxLines) {
                val lastLineStart = textView.layout?.getLineStart(maxLines - 1) ?: 0
                val lastLineEnd = textView.layout?.getLineEnd(maxLines - 1) ?: text.length
                val lastLineLength = lastLineEnd - lastLineStart
                val truncatedText = text.substring(0, lastLineStart + (lastLineLength / 2))
                val showMoreText = getString(R.string.show_more)
                val showLessText = getString(R.string.show_less)
                val spannableStringShowMore = SpannableString("$truncatedText.. $showMoreText")
                val spannableStringShowLess = SpannableString("$text $showLessText")

                val clickableSpanShowMore = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        textView.maxLines = Int.MAX_VALUE
                        textView.text = spannableStringShowLess
                    }
                }

                val clickableSpanShowLess = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        textView.maxLines = maxLines
                        textView.text = spannableStringShowMore
                    }
                }

                val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.purple))

                spannableStringShowMore.setSpan(clickableSpanShowMore, truncatedText.length + 3, spannableStringShowMore.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringShowMore.setSpan(colorSpan, truncatedText.length + 3, spannableStringShowMore.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                spannableStringShowLess.setSpan(clickableSpanShowLess, text.length + 1, spannableStringShowLess.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringShowLess.setSpan(colorSpan, text.length + 1, spannableStringShowLess.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                textView.text = spannableStringShowMore
                textView.movementMethod = LinkMovementMethod.getInstance()
            } else {
                textView.text = text
            }
        }
    }
}
