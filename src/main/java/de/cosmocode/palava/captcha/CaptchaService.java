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

/**
 * A service for generating captchas.
 *
 * @author Willi Schoenborn
 */
public interface CaptchaService {

    /**
     * Returns the byte data of the captcha with the given token.
     * 
     * @param token the token
     * @return byte array of jpeg image data
     */
    byte[] getCaptcha(String token);

    /**
     * Checks for valid user inputs.
     * 
     * @param token the token
     * @param userInput the user's input
     * @return true if userInput was valid
     */
    boolean validate(String token, String userInput);

}
