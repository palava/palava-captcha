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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.cosmocode.palava.ipc.IpcCall;
import de.cosmocode.palava.ipc.IpcCommand;
import de.cosmocode.palava.ipc.IpcCommand.Description;
import de.cosmocode.palava.ipc.IpcCommand.Param;
import de.cosmocode.palava.ipc.IpcCommand.Return;
import de.cosmocode.palava.ipc.IpcCommandExecutionException;
import de.cosmocode.palava.ipc.IpcConnection;
import de.cosmocode.palava.ipc.IpcSession;

/**
 * See below.
 *
 * @author Willi Schoenborn
 */
@Description("Checks a user input against a captcha challenge.")
@Param(name = "code", description = "The user input")
@Return(name = "valid", description = "true if user input was valid, false otherwise")
@Singleton
public final class Validate implements IpcCommand {

    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);

    private final CaptchaService captcha;
    
    @Inject
    public Validate(CaptchaService captcha) {
        this.captcha = Preconditions.checkNotNull(captcha, "Captcha");
    }

    @Override
    public void execute(IpcCall call, Map<String, Object> result) throws IpcCommandExecutionException {

        final IpcConnection connection = call.getConnection();
        final IpcSession session = connection.getSession();
        final String sessionId = session.getSessionId();
        
        final String userInput = call.getArguments().getString("code");
        
        final boolean valid = captcha.validate(sessionId, userInput);
        LOG.debug("User input '{}' was valid: {}", userInput, Boolean.valueOf(valid));

        result.put("valid", Boolean.valueOf(valid));
    }

}
