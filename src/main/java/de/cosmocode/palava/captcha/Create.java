/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.captcha;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.cosmocode.palava.ipc.IpcCall;
import de.cosmocode.palava.ipc.IpcCommand;
import de.cosmocode.palava.ipc.IpcCommandExecutionException;
import de.cosmocode.palava.ipc.IpcConnection;
import de.cosmocode.palava.ipc.IpcSession;
import de.cosmocode.palava.ipc.IpcCommand.Description;
import de.cosmocode.palava.ipc.IpcCommand.Return;

/**
 * See below.
 *
 * @author Willi Schoenborn
 */
@Description("Creates a new captcha.")
@Return(
    name = "base64",
    description = "A base64 encoded captcha challenge"
)
@Singleton
public final class Create implements IpcCommand {

    private static final Logger LOG = LoggerFactory.getLogger(Create.class);

    private final CaptchaService captcha;
    
    @Inject
    public Create(CaptchaService captcha) {
        this.captcha = Preconditions.checkNotNull(captcha, "Captcha");
    }
    
    @Override
    public void execute(IpcCall call, Map<String, Object> result) throws IpcCommandExecutionException {

        final IpcConnection connection = call.getConnection();
        final IpcSession session = connection.getSession();
        final String sessionId = session.getSessionId();
        
        final byte[] binary = captcha.getCaptcha(sessionId);
        LOG.trace("Created captcha challenge for {}", sessionId);
        final String base64 = Base64.encodeBase64String(binary);
        
        result.put("base64", base64);
    }

}
