package toyproject.startofconversation.common.support

fun normalize(input: String): String =
    input.trim().replace(Regex("\\s+"), " ").lowercase()