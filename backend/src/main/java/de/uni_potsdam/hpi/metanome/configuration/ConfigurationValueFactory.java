/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.*;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SqlInputGenerator;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Converts the incoming {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification}s to {@link de.uni_potsdam.hpi.metanome.configuration.ConfigurationValue}s.
 */
public class ConfigurationValueFactory {

    /**
     * Converts the incoming {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification}s to {@link de.uni_potsdam.hpi.metanome.configuration.ConfigurationValue}s.
     *
     * @param specification the specification to convert
     * @return the created configuration value
     * @throws AlgorithmConfigurationException
     */
    public static ConfigurationValue createConfigurationValue(
            ConfigurationSpecification specification) throws AlgorithmConfigurationException {

        if (specification instanceof ConfigurationSpecificationBoolean) {
            return new ConfigurationValueBoolean((ConfigurationSpecificationBoolean) specification);
        } else if (specification instanceof ConfigurationSpecificationCsvFile) {
            return new ConfigurationValueFileInputGenerator(specification.getIdentifier(),
                    createFileInputGenerators((ConfigurationSpecificationCsvFile) specification));
        } else if (specification instanceof ConfigurationSpecificationSqlIterator) {
            return new ConfigurationValueSqlInputGenerator(specification.getIdentifier(),
                    createSqlIteratorGenerators((ConfigurationSpecificationSqlIterator) specification));
        } else if (specification instanceof ConfigurationSpecificationString) {
            return new ConfigurationValueString((ConfigurationSpecificationString) specification);
        } else {
            throw new AlgorithmConfigurationException("Unsupported ConfigurationSpecification subclass.");
        }
    }

    /**
     * Converts a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator} to a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.SqlInputGenerator}.
     *
     * @param specification the sql iterator specification
     * @return the created sql input generator
     * @throws AlgorithmConfigurationException
     */
    private static SqlInputGenerator[] createSqlIteratorGenerators(
            ConfigurationSpecificationSqlIterator specification) throws AlgorithmConfigurationException {

        SqlIteratorGenerator[] sqlIteratorGenerators = new SqlIteratorGenerator[specification.getSettings().length];

        int i = 0;
        for (ConfigurationSettingSqlIterator setting : specification.getSettings()) {
            sqlIteratorGenerators[i] = new SqlIteratorGenerator(setting.getDbUrl(),
                    setting.getUsername(), setting.getPassword());
            i++;
        }
        return sqlIteratorGenerators;
    }

    /**
     * Converts a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile} to a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator}.
     *
     * @param specification the file input specification
     * @return the created file input generator
     * @throws AlgorithmConfigurationException
     */
    private static FileInputGenerator[] createFileInputGenerators(
            ConfigurationSpecificationCsvFile specification) throws AlgorithmConfigurationException {

        CsvFileGenerator[] csvFileGenerators = new CsvFileGenerator[specification.getSettings().length];

        int i = 0;
        for (ConfigurationSettingCsvFile setting : specification.getSettings()) {
            try {
                if (setting.isAdvanced())
                    // FIXME fix header parameter
                    csvFileGenerators[i] = new CsvFileGenerator(new File(setting.getFileName()), setting.getSeparatorChar(),
                            setting.getQuoteChar(), setting.getEscapeChar(), setting.getLine(),
                            setting.isStrictQuotes(), setting.isIgnoreLeadingWhiteSpace(), true);
                else
                    csvFileGenerators[i] = new CsvFileGenerator(new File(setting.getFileName()));
            } catch (FileNotFoundException e) {
                throw new AlgorithmConfigurationException("Could not find CSV file.");
            }
            i++;
        }

        return csvFileGenerators;
    }

}