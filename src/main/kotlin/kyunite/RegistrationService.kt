package kyunite

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class RegistrationQuery(var found: Boolean,
                             var discordID: String?,
                             var isLinked: Boolean,
                             var epicID: String?,
                             var epicDisplayName: String?,
                             var discriminatedDiscordName: String?)

data class BulkRegistrationQuery(var users: List<RegistrationQuery>,
                                 var invalidIDs: List<String>)

interface RegistrationService {

    @GET("v2/servers/GUILD_ID/regsys/single/byDiscordID/{USER_ID}")
    fun singleUserLink(@Path("USER_ID") discordId: Long): Call<RegistrationQuery>

    @GET("v2/servers/GUILD_ID/regsys/single/byEpicID/{USER_ID}")
    fun singleUserLink(@Path("USER_ID") epicId: String): Call<RegistrationQuery>

    @POST("v2/servers/GUILD_ID/regsys/bulk/{QUERY_TYPE}")
    fun bulkUserLink(@Path("QUERY_TYPE") type: String = "byEpicID", @Body ids: List<String>): Call<BulkRegistrationQuery>

    fun bulkUserLink(@Body ids: List<Long>): Call<BulkRegistrationQuery> = bulkUserLink("byDiscordID", ids.map { "$it" })

}