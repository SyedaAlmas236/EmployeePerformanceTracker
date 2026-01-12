package com.example.employeeperformancetracker.data

import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

object ImageRepository {
    private val supabase = SupabaseConfig.client

    // Uploads an image byte array to the specified bucket and returns the public URL
    suspend fun uploadImage(byteArray: ByteArray, bucketName: String = "profiles"): Result<String> {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val bucket = supabase.storage.from(bucketName)
            
            withContext(Dispatchers.IO) {
                bucket.upload(fileName, byteArray)
            }
            
            val publicUrl = bucket.publicUrl(fileName)
            println("ImageRepository: Upload success, URL: $publicUrl")
            Result.success(publicUrl)
        } catch (e: Exception) {
            println("ImageRepository: Upload failed: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
