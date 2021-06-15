package fr.epita.assistants.ui.utils

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle


class CodeHighlight(val colors: Colors) : VisualTransformation {
    val offsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset
        }

    }

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(codeString(text.toString()), offsetMapping)
    }

    private fun codeString(str: String) = buildAnnotatedString {
        withStyle(SpanStyle(colors.onSecondary)) {
            val strFormatted = str.replace("\t", "    ")
            append(strFormatted)
            addStyle(colors.primaryVariant, strFormatted, ":")
            addStyle(colors.primaryVariant, strFormatted, "=")
            addStyle(colors.primaryVariant, strFormatted, "\"")
            addStyle(colors.primaryVariant, strFormatted, "[")
            addStyle(colors.primaryVariant, strFormatted, "]")
            addStyle(colors.primaryVariant, strFormatted, "{")
            addStyle(colors.primaryVariant, strFormatted, "}")
            addStyle(colors.primaryVariant, strFormatted, "(")
            addStyle(colors.primaryVariant, strFormatted, ")")
            addStyle(colors.primaryVariant, strFormatted, ",")
            addStyle(colors.primaryVariant, strFormatted, "static ")
            addStyle(colors.primaryVariant, strFormatted, "if ")
            addStyle(colors.primaryVariant, strFormatted, "for ")
            addStyle(colors.primaryVariant, strFormatted, "try ")
            addStyle(colors.primaryVariant, strFormatted, "catch ")
            addStyle(colors.primaryVariant, strFormatted, "new ")

            addStyle(colors.secondaryVariant, strFormatted, "fun ")
            addStyle(colors.secondaryVariant, strFormatted, "final ")
            addStyle(colors.secondaryVariant, strFormatted, "val ")
            addStyle(colors.secondaryVariant, strFormatted, "var ")
            addStyle(colors.secondaryVariant, strFormatted, "private ")
            addStyle(colors.secondaryVariant, strFormatted, "internal ")
            addStyle(colors.secondaryVariant, strFormatted, "expect ")
            addStyle(colors.secondaryVariant, strFormatted, "actual ")
            addStyle(colors.secondaryVariant, strFormatted, "import ")
            addStyle(colors.secondaryVariant, strFormatted, "package ")

            addStyle(colors.onBackground, strFormatted, "true")
            addStyle(colors.onBackground, strFormatted, "false")
            addStyle(colors.onBackground, strFormatted, Regex(" [0-9]*"))
            addStyle(colors.onBackground, strFormatted, Regex("@[a-zA-Z_]*"))
            addStyle(colors.onBackground, strFormatted, Regex(" +//.*"))

            addStyle(colors.onBackground, strFormatted, "public ")
            addStyle(colors.onBackground, strFormatted, "return ")
            addStyle(colors.onBackground, strFormatted, "-> ")

            addStyle(colors.onBackground, strFormatted, Regex(" int "))
            addStyle(colors.onBackground, strFormatted, Regex(" boolean "))
            addStyle(colors.onBackground, strFormatted, Regex(" float "))
            addStyle(colors.onBackground, strFormatted, Regex(" long "))
            addStyle(colors.onBackground, strFormatted, Regex(" byte "))
            addStyle(colors.onBackground, strFormatted, Regex(" short "))
            addStyle(colors.onBackground, strFormatted, Regex(" double "))
            addStyle(colors.onBackground, strFormatted, Regex(" char "))

            addStyle(colors.onBackground, strFormatted, Regex("Integer "))
            addStyle(colors.onBackground, strFormatted, Regex("String "))
            addStyle(colors.onBackground, strFormatted, Regex("Boolean "))
            addStyle(colors.onBackground, strFormatted, Regex("Float "))
            addStyle(colors.onBackground, strFormatted, Regex("Long "))
            addStyle(colors.onBackground, strFormatted, Regex("Double "))
            addStyle(colors.onBackground, strFormatted, Regex("Char "))
            addStyle(colors.onBackground, strFormatted, Regex("Short "))
            addStyle(colors.onBackground, strFormatted, Regex("Byte "))
        }
    }

    private fun AnnotatedString.Builder.addStyle(style: Color, text: String, regexp: String) {
        addStyle(style, text, Regex.fromLiteral(regexp))
    }

    private fun AnnotatedString.Builder.addStyle(style: Color, text: String, regexp: Regex) {
        for (result in regexp.findAll(text)) {
            addStyle(SpanStyle(style), result.range.first, result.range.last + 1)
        }
    }
}