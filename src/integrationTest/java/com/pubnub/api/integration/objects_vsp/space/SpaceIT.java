package com.pubnub.api.integration.objects_vsp.space;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.SpaceId;
import com.pubnub.api.integration.objects.ObjectsApiBaseIT;
import com.pubnub.api.models.consumer.objects_vsp.space.RemoveSpaceResult;
import com.pubnub.api.models.consumer.objects_vsp.space.Space;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpaceIT extends ObjectsApiBaseIT {

    private final String randomSpaceIdValue = getRandomSpaceIdValue();
    private SpaceId randomSpaceId;
    private final String randomName = randomName();
    private final String randomDescription = randomDescription();

    @Before
    public void setUp() throws Exception {
        randomSpaceId = new SpaceId(randomSpaceIdValue);
    }

    @Test
    public void createUserHappyPath() throws PubNubException {
        //given

        //when
        Space space = pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //then
        assertNotNull(space);
        assertEquals(randomSpaceIdValue, space.getId());
        assertEquals(randomName, space.getName());
        assertEquals(randomDescription, space.getDescription());
        assertNotNull(space.getCustom());
        assertEquals(STATUS_ACTIVE, space.getStatus());
        assertEquals(TYPE_HUMAN, space.getType());
    }

    @Test(expected = PubNubException.class)
    public void should_throw_exception_when_space_with_the_spaceId_exists() throws PubNubException {
        //given
        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //when
        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //then
    }

    @Test
    public void removeSpaceHappyPath() throws PubNubException {
        //given
        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //when
        RemoveSpaceResult removeSpaceResult = pubNubUnderTest.removeSpace(randomSpaceId).sync();

        //then
        assertEquals(HttpStatus.SC_OK, removeSpaceResult.getStatus());


    }

    @Test
    public void fetchSpaceHappyPath() throws PubNubException {
        //given
        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //when
        Space space = pubNubUnderTest.fetchSpace(randomSpaceId)
                .sync();

        //then
        assertNotNull(space);
        assertEquals(randomSpaceIdValue, space.getId());
        assertEquals(randomName, space.getName());
        assertEquals(randomDescription, space.getDescription());
        assertNotNull(space.getCustom());
        assertEquals(STATUS_ACTIVE, space.getStatus());
        assertEquals(TYPE_HUMAN, space.getType());

    }

    @Test
    public void updateSpace_passing_full_object_happyPath() throws PubNubException {
        //given
        String updatedName = "updatedName" + randomName();
        String updatedDescription = "updatedDescription" + randomName();
        Map<String, Object> updateCustom = updatedCustomSpaceObject();
        String updatedStatus = "updatedStatus" + STATUS_ACTIVE;
        String updatedType = "updatedType" + TYPE_HUMAN;

        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //when
        Space updatedSpace = pubNubUnderTest.updateSpace(randomSpaceId)
                .name(updatedName)
                .description(updatedDescription)
                .custom(updateCustom)
                .status(updatedStatus)
                .type(updatedType)
                .sync();

        //then
        assertNotNull(updatedSpace);
        assertEquals(randomSpaceIdValue, updatedSpace.getId());
        assertEquals(updatedName, updatedSpace.getName());
        assertEquals(updatedDescription, updatedSpace.getDescription());
        assertEquals(updatedDescription, updatedSpace.getDescription());
        assertEquals("\"val1_updated\"", ((JsonObject) updatedSpace.getCustom()).getAsJsonObject().get("param1").toString());
        assertEquals("\"val2_updated\"", ((JsonObject) updatedSpace.getCustom()).getAsJsonObject().get("param2").toString());
        assertEquals("\"added\"", ((JsonObject) updatedSpace.getCustom()).getAsJsonObject().get("param3").toString());
        assertEquals(updatedStatus, updatedSpace.getStatus());
        assertEquals(updatedType, updatedSpace.getType());

        Space space = pubNubUnderTest.fetchSpace(randomSpaceId)
                .sync();

        assertNotNull(space);
        assertEquals(randomSpaceIdValue, space.getId());
        assertEquals(updatedName, space.getName());
        assertEquals(updatedDescription, space.getDescription());
        assertNotNull(space.getCustom());
        assertEquals(updatedStatus, space.getStatus());
        assertEquals(updatedType, space.getType());
    }

    @Test(expected = PubNubException.class)
    public void updateSpace_should_throw_exception_when_updating_space_that_does_not_exist() throws PubNubException {
        //given
        String updatedName = "updatedName" + randomName();

        //when
        pubNubUnderTest.updateSpace(randomSpaceId)
                .name(updatedName)
                .sync();

        //then

    }

    @Test
    public void upsertSpaceHappyPath_newSpaceCreated() throws PubNubException {
        //given

        //when
        Space space = pubNubUnderTest.upsertSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //then
        assertNotNull(space);
        assertEquals(randomSpaceIdValue, space.getId());
        assertEquals(randomName, space.getName());
        assertEquals(randomDescription, space.getDescription());
        assertNotNull(space.getCustom());
        assertEquals(STATUS_ACTIVE, space.getStatus());
        assertEquals(TYPE_HUMAN, space.getType());
    }

    @Test
    public void upsertSpaceHappyPath_existingSpaceUpdated() throws PubNubException {
        //given
        String updatedName = "updatedName" + randomName();
        String updatedDescription = "updatedDescription" + randomName();
        Map<String, Object> updateCustom = updatedCustomSpaceObject();
        String updatedStatus = "updatedStatus" + STATUS_ACTIVE;
        String updatedType = "updatedType" + TYPE_HUMAN;

        pubNubUnderTest.createSpace(randomSpaceId)
                .name(randomName)
                .description(randomDescription)
                .custom(customSpaceObject())
                .includeCustom(true)
                .status(STATUS_ACTIVE)
                .type(TYPE_HUMAN)
                .sync();

        //when
        Space spaceAfterUpsert = pubNubUnderTest.upsertSpace(randomSpaceId)
                .name(updatedName)
                .description(updatedDescription)
                .custom(updateCustom)
                .status(updatedStatus)
                .type(updatedType)
                .sync();

        //then
        assertNotNull(spaceAfterUpsert);
        assertEquals(randomSpaceIdValue, spaceAfterUpsert.getId());
        assertEquals(updatedName, spaceAfterUpsert.getName());
        assertEquals(updatedDescription, spaceAfterUpsert.getDescription());
        assertEquals(updatedDescription, spaceAfterUpsert.getDescription());
        assertEquals("\"val1_updated\"", ((JsonObject) spaceAfterUpsert.getCustom()).getAsJsonObject().get("param1").toString());
        assertEquals("\"val2_updated\"", ((JsonObject) spaceAfterUpsert.getCustom()).getAsJsonObject().get("param2").toString());
        assertEquals("\"added\"", ((JsonObject) spaceAfterUpsert.getCustom()).getAsJsonObject().get("param3").toString());
        assertEquals(updatedStatus, spaceAfterUpsert.getStatus());
        assertEquals(updatedType, spaceAfterUpsert.getType());

        Space space = pubNubUnderTest.fetchSpace(randomSpaceId)
                .sync();

        assertNotNull(space);
        assertEquals(randomSpaceIdValue, space.getId());
        assertEquals(updatedName, space.getName());
        assertEquals(updatedDescription, space.getDescription());
        assertNotNull(space.getCustom());
        assertEquals(updatedStatus, space.getStatus());
        assertEquals(updatedType, space.getType());
    }

    @After
    public void tearDown() throws Exception {
        pubNubUnderTest.removeSpace(randomSpaceId).sync();
    }

    private String getRandomSpaceIdValue() {
        return "spaceId" + new Random().nextInt(100000);
    }

    private static String randomDescription() {
        return RandomStringUtils.randomAlphabetic(50, 160);
    }
}
