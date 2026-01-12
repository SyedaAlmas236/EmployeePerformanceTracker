package com.example.employeeperformancetracker.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseConfig {
    private const val SUPABASE_URL = "https://kdfasxbmxodzpldkbgda.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtkZmFzeGJteG9kenBsZGtiZ2RhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc3MTM0NTQsImV4cCI6MjA4MzI4OTQ1NH0.equFNy97raHHBteMEOZQoreVZYI2mehQeWkbJuCIbd4"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
