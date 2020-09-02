/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.comfoair.internal.datatypes;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.comfoair.internal.ComfoAirCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle revolutions per minute values
 *
 * @author Grzegorz Miasko - Initial Contribution
 * @author Hans Böhm - Refactoring
 */
@NonNullByDefault
public class DataTypeRPM implements ComfoAirDataType {
    private static final DataTypeRPM SINGLETON_INSTANCE = new DataTypeRPM();

    private DataTypeRPM() {
    }

    private final Logger logger = LoggerFactory.getLogger(DataTypeRPM.class);

    public static DataTypeRPM getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public State convertToState(int @Nullable [] data, ComfoAirCommandType commandType) {
        if (data == null) {
            logger.trace("\"DataTypeRPM\" class \"convertToState\" method parameter: null");
            return UnDefType.NULL;
        } else {
            int value = calculateNumberValue(data, commandType);

            if (value < 0) {
                return UnDefType.NULL;
            }
            // transferred value is (1875000 / rpm) per protocol
            return new DecimalType((int) (1875000.0 / value));
        }
    }

    @Override
    public int @Nullable [] convertFromState(State value, ComfoAirCommandType commandType) {
        if (value instanceof UnDefType) {
            logger.trace("\"DataTypeRPM\" class \"convertFromState\" undefined state");
            return null;
        } else {
            int[] template = commandType.getChangeDataTemplate();
            // transferred value is (1875000 / rpm) per protocol
            template[commandType.getChangeDataPos()] = (int) (1875000 / ((DecimalType) value).doubleValue());
            return template;
        }
    }
}