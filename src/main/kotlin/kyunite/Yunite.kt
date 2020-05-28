package kyunite

import com.google.common.collect.HashBiMap
import jdk.nashorn.internal.ir.Block
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Yunite(apiKey: String, guildId: Long) {

    internal class YInterceptor(val apiKey: String, val guildId: Long): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (chain.request().url().host() != "yunite.xyz") {
                return chain.proceed(chain.request())
            }
            return chain.proceed(chain.request()
                .newBuilder()
                .addHeader("Y-Api-Key", apiKey)
                .url(chain.request().url().toString().replace("GUILD_ID", "$guildId"))
                .build())
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://yunite.xyz/api/")
        .client(OkHttpClient.Builder().addInterceptor(YInterceptor(apiKey, guildId)).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val blockService = retrofit.create(BlockService::class.java)
    private val registrationService = retrofit.create(RegistrationService::class.java)

    private val cachedUsers = HashBiMap.create<Long, String>()

    fun bulkDiscordCache(ids: List<Long>) {
        registrationService.bulkUserLink(ids).execute().body()?.users?.forEach {
            cachedUsers[it.discordID?.toLong()] = it.epicID
        }
    }

    fun bulkEpicCache(epicIds: List<String>) {
        registrationService.bulkUserLink(ids = epicIds).execute().body()?.users?.forEach {
            cachedUsers[it.discordID?.toLong()] = it.epicID
        }
    }

    operator fun get(discordId: Long): String? {
        if (cachedUsers.containsKey(discordId)) {
            return cachedUsers[discordId]!!
        }
        val query = registrationService.singleUserLink(discordId).execute()
        cachedUsers[discordId] = query.body()!!.epicID
        return query.body()!!.epicID
    }

    operator fun get(epicId: String): Long? {
        if (cachedUsers.containsValue(epicId)) {
            return cachedUsers.inverse()[epicId]!!
        }
        val query = registrationService.singleUserLink(epicId).execute()
        cachedUsers.inverse()[epicId] = query.body()!!.discordID!!.toLong()
        return query.body()!!.discordID?.toLong()
    }

}