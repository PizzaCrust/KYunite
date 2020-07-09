package kyunite

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class BlockResponse(var status: String,
                         var unlinkedUser: String?)

interface BlockService {

    @POST("v1/servers/GUILD_ID/regsys/user/{USER_ID}/block")
    fun block(@Path("USER_ID") id: String,
              @Query("blockLinkedAccount") blockedLinkedAccount: Boolean = false,
              @Query("reason", encoded = true) reason: String): Call<BlockResponse>

    @DELETE("v1/servers/GUILD_ID/regsys/user/{USER_ID}/block")
    fun unblock(@Path("USER_ID") id: String): Call<BlockResponse>

}