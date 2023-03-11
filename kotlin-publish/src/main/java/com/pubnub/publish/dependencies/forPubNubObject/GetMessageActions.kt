package com.pubnub.publish.dependencies.forPubNubObject


import com.pubnub.publish.dependencies.*
import com.pubnub.publish.dependencies.other.PNGetMessageActionsResult
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.getMessageActions]
 */
class GetMessageActions internal constructor(
    pubnub: PubNub,
    val channel: String,
    val page: PNBoundedPage
) : Endpoint<PNGetMessageActionsResult, PNGetMessageActionsResult>(pubnub) {

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) throw PubNubException(PubNubError.CHANNEL_MISSING)
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<PNGetMessageActionsResult> {

        addQueryParams(queryParams)

        return pubnub.retrofitManager.messageActionService
            .getMessageActions(
                pubnub.configuration.subscribeKey,
                channel,
                queryParams
            )
    }

    override fun createResponse(input: Response<PNGetMessageActionsResult>): PNGetMessageActionsResult =
        PNGetMessageActionsResult(
            actions = input.body()!!.actions,
            page = input.body()!!.page
        )

    override fun operationType() = PNOperationType.PNGetMessageActions

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        page.start?.run { queryParams["start"] = this.toString().toLowerCase() }
        page.end?.run { queryParams["end"] = this.toString().toLowerCase() }
        page.limit?.run { queryParams["limit"] = this.toString().toLowerCase() }
    }
}