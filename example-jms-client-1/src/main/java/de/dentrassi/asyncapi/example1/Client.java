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

import de.dentrassi.asyncapi.ListenerHandle;
import de.dentrassi.asyncapi.Connector.Builder;
import de.dentrassi.asyncapi.gson.GsonPayloadFormat;
import de.dentrassi.asyncapi.jms.amqp.AmqpProfile;
import hitch.jms.client.JmsClient;

public class Client {
    public static void main(final String[] args) throws Exception {

        final Builder<JmsClient> builder = JmsClient.newBuilder()
                .host("localhost")
                .profile(AmqpProfile.DEFAULT_PROFILE)
                .payloadFormat(new GsonPayloadFormat(gson -> {
                    gson.setPrettyPrinting();
                }));

        try (final JmsClient client = builder.build()) {

            try (ListenerHandle listener = client.accounts().eventUserSignup().subscribe(System.out::println)) {

                listener.toCompletableFuture().get();

                System.out.println("Waiting for messages…");

                Thread.sleep(Long.MAX_VALUE);
            }

        }
    }

}
