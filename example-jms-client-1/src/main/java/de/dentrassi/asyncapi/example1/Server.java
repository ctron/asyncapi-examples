/*
 * Copyright (C) 2017 Jens Reimann <jreimann@redhat.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.dentrassi.asyncapi.example1;

import java.time.ZonedDateTime;

import de.dentrassi.asyncapi.Connector.Builder;
import de.dentrassi.asyncapi.gson.GsonPayloadFormat;
import de.dentrassi.asyncapi.jms.amqp.AmqpProfile;
import hitch.jms.server.JmsServer;
import hitch.messages.UserSignedUp;
import hitch.types.Signup;
import hitch.types.Signup.Method;
import hitch.types.User;

public class Server {
    public static void main(final String[] args) throws Exception {

        final Builder<JmsServer> builder = JmsServer.newBuilder()
                .host("localhost")
                .profile(AmqpProfile.DEFAULT_PROFILE)
                .payloadFormat(new GsonPayloadFormat(gson -> {
                    gson.setPrettyPrinting();
                }));

        try (final JmsServer client = builder.build()) {

            client.accounts().eventUserSignup().publish(newUserMessage())
                    .toCompletableFuture()
                    .get();

        }
    }

    private static UserSignedUp newUserMessage() {
        final UserSignedUp message = new UserSignedUp();

        final UserSignedUp.Payload payload = new UserSignedUp.Payload();
        message.setPayload(payload);

        final Signup signup = new Signup();
        signup.setDatetime(ZonedDateTime.now());
        signup.setMethod(Method.EMAIL);

        payload.setSignup(signup);

        final User user = new User();
        user.setId("id");
        user.setFullName("Foo Bar");
        user.setUsername("foobar");

        payload.setUser(user);

        return message;
    }
}
