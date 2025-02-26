package com.example.tareafinal101224.utils

fun String .encodeEmail() = this.replace("@", "_AT_").replace(".", "_DOT_")