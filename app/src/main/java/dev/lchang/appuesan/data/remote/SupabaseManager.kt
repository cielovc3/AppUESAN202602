package dev.lchang.appuesan.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.realtime.Realtime

object SupabaseManager {

    private const val SUPABASE_URL = "https://zhktnxlkcvbmwjlrvdmq.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inpoa3RueGxrY3ZibXdqbHJ2ZG1xIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTIyNzI0NDQsImV4cCI6MjEwNzg0ODQ0NH0.5EwHISoYX85GDhKA1o-ITotYA1CImCgbm1dJCgWUbaM"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
        install(Realtime)
    }
}
