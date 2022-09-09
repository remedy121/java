package com.pubnub.api.endpoints.objects_vsp.user;

import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_vsp.UserEndpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.objects_vsp.user.RemoveUserResult;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Collections;
import java.util.Map;

public class RemoveUser extends UserEndpoint<RemoveUser, EntityEnvelope<JsonElement>, RemoveUserResult> {

    public RemoveUser(
            final PubNub pubnubInstance,
            final TelemetryManager telemetry,
            final RetrofitManager retrofitInstance,
            final TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, CompositeParameterEnricher.createDefault(), tokenManager);
    }

    @Override
    protected Call<EntityEnvelope<JsonElement>> executeCommand(Map<String, String> effectiveParams) throws PubNubException {
        return getRetrofit()
                .getUserService()
                .removeUser(getPubnub().getConfiguration().getSubscribeKey(), effectiveUserId().getValue(), Collections.emptyMap());
    }

    @Override
    protected RemoveUserResult createResponse(Response<EntityEnvelope<JsonElement>> input) throws PubNubException {
        return new RemoveUserResult(input.body());
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNRemoveUserOperation;
    }
}
