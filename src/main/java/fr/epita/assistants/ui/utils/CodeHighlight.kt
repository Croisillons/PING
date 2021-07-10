package fr.epita.assistants.ui.utils

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import fr.epita.assistants.ui.store.ProjectStore


class CodeHighlight(val colors: Colors, val projectStore: ProjectStore) : VisualTransformation {
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
            append(str)
            //addStyle(SpanStyle(Color.Red), 2, 4)
            addStyle(colors.primaryVariant, str, ":")
            addStyle(colors.onBackground, str, "=")
            addStyle(colors.primaryVariant, str, "\"")
            addStyle(colors.primaryVariant, str, "[")
            addStyle(colors.primaryVariant, str, "]")
            addStyle(colors.primaryVariant, str, "{")
            addStyle(colors.primaryVariant, str, "}")
            addStyle(colors.primaryVariant, str, "(")
            addStyle(colors.primaryVariant, str, ")")
            addStyle(colors.primaryVariant, str, ",")
            addStyle(colors.primaryVariant, str, " static ")
            addStyle(colors.primaryVariant, str, " if ")
            addStyle(colors.primaryVariant, str, " for ")
            addStyle(colors.primaryVariant, str, " try ")
            addStyle(colors.primaryVariant, str, "catch ")
            addStyle(colors.primaryVariant, str, " new ")
            addStyle(colors.primaryVariant, str, " implements ")
            addStyle(colors.primaryVariant, str, " extends ")

            addStyle(colors.secondaryVariant, str, "fun ")
            addStyle(colors.secondaryVariant, str, "final ")
            addStyle(colors.secondaryVariant, str, " val ")
            addStyle(colors.secondaryVariant, str, " var ")
            addStyle(colors.secondaryVariant, str, "private ")
            addStyle(colors.secondaryVariant, str, "internal ")
            addStyle(colors.secondaryVariant, str, "expect ")
            addStyle(colors.secondaryVariant, str, "actual ")
            addStyle(colors.secondaryVariant, str, "import ")
            addStyle(colors.secondaryVariant, str, "package ")
            addStyle(colors.secondaryVariant, str, " class ")
            addStyle(colors.secondaryVariant, str, " interface ")
            addStyle(colors.secondaryVariant, str, " enum ")
            addStyle(colors.secondaryVariant, str, " throw ")
            addStyle(colors.secondaryVariant, str, " throws")
            addStyle(colors.secondaryVariant, str, Regex("<[^>]*>"))
            addStyle(colors.primaryVariant, str, Regex("\"[^\"]*\""))

            addStyle(colors.onBackground, str, "true")
            addStyle(colors.onBackground, str, "false")
            addStyle(colors.onBackground, str, Regex(" [0-9]*"))
            addStyle(colors.secondaryVariant, str, Regex("@[a-zA-Z_]*"))
            addStyle(colors.onBackground, str, Regex(" +//.*"))

            addStyle(colors.onBackground, str, "public ")
            addStyle(colors.onBackground, str, "return ")
            addStyle(colors.onBackground, str, "-> ")

            addStyle(colors.onBackground, str, Regex(" int "))
            addStyle(colors.onBackground, str, Regex(" void "))
            addStyle(colors.onBackground, str, Regex(" boolean "))
            addStyle(colors.onBackground, str, Regex(" float "))
            addStyle(colors.onBackground, str, Regex(" long "))
            addStyle(colors.onBackground, str, Regex(" byte "))
            addStyle(colors.onBackground, str, Regex(" short "))
            addStyle(colors.onBackground, str, Regex(" double "))
            addStyle(colors.onBackground, str, Regex(" char "))

            addStyle(colors.onBackground, str, Regex("Integer "))
            addStyle(colors.onBackground, str, Regex("String "))
            addStyle(colors.onBackground, str, Regex("Boolean "))
            addStyle(colors.onBackground, str, Regex("Float "))
            addStyle(colors.onBackground, str, Regex("Long "))
            addStyle(colors.onBackground, str, Regex("Double "))
            addStyle(colors.onBackground, str, Regex("Char "))
            addStyle(colors.onBackground, str, Regex("Short "))
            addStyle(colors.onBackground, str, Regex("Byte "))
            addStyle(colors.onBackground, str, Regex(" ArrayList"))
            addStyle(colors.onBackground, str, Regex(" List"))
            addStyle(colors.onBackground, str, Regex(" HashMap"))
            addStyle(colors.onBackground, str, Regex(" Map"))
            addStyle(colors.onBackground, str, Regex(" Set"))
            addStyle(colors.onBackground, str, Regex(" HashSet"))

            addStyle(colors.onSecondary, str, Regex("\\|"))

            for (diagnostic in projectStore.diagnostics)
            {
                if (diagnostic.startPosition == diagnostic.endPosition)
                {
                    addStyle(SpanStyle(Color.Red, textDecoration = TextDecoration.Underline), diagnostic.startPosition.toInt() - 1, diagnostic.endPosition.toInt())
                }
                else
                {
                    val start = diagnostic.startPosition.coerceAtMost(diagnostic.endPosition);
                    val end = diagnostic.startPosition.coerceAtLeast(diagnostic.endPosition);
                    addStyle(SpanStyle(Color.Red, textDecoration = TextDecoration.Underline), start.toInt(), end.toInt())
                }
            }
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