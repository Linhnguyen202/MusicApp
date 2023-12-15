package com.example.musicapp.utils

import okio.IOException

class NoInternetException(message : String) : IOException(message)