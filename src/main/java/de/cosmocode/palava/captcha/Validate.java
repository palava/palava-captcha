/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.captcha;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.palava.ipc.IpcCall;
import de.cosmocode.palava.ipc.IpcCommand;
import de.cosmocode.palava.ipc.IpcCommandExecutionException;
import de.cosmocode.palava.ipc.IpcConnection;
import de.cosmocode.palava.ipc.IpcSession;
import de.cosmocode.palava.ipc.IpcCommand.Description;
import de.cosmocode.palava.ipc.IpcCommand.Param;
import de.cosmocode.palava.ipc.IpcCommand.Return;
import de.cosmocode.palava.services.captcha.Captcha;

/**
 * See below.
 *
 * @author Willi Schoenborn
 */
@Description("Checks a user input against a captcha challenge.")
@Param(name = "code", description = "The user input")
@Return(name = "valid", description = "true if user input was valid, false otherwise")
public final class Validate implements IpcCommand {

    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);

    private final Captcha captcha;
    
    @Inject
    public Validate(Captcha captcha) {
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
