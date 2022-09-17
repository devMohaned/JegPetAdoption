package com.jeg.pet.app.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

const val GOOGLE_DNS: String = "8.8.8.8"
const val PORT_DNS: Int = 53
// TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
fun isOnline(): Boolean {
    return try {
        val timeoutMs = 1500
        val sock = Socket()
        val sockaddr: SocketAddress = InetSocketAddress(GOOGLE_DNS, PORT_DNS)
        sock.connect(sockaddr, timeoutMs)
        sock.close()
        true
    } catch (e: IOException) {
        false
    }
}