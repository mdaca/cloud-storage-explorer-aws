/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.codegen.jmespath.parser.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import software.amazon.awssdk.codegen.jmespath.parser.ParseError;
import software.amazon.awssdk.codegen.jmespath.parser.ParseResult;
import software.amazon.awssdk.codegen.jmespath.parser.Parser;
import software.amazon.awssdk.codegen.jmespath.parser.ParserContext;

public final class CompositeParser<T> implements Parser<T> {
    private final String name;
    private final List<Parser<T>> parsers;

    @SafeVarargs
    public CompositeParser(String name, Parser<T>... parsers) {
        this.name = name;
        this.parsers = Arrays.asList(parsers);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ParseResult<T> parse(int startPosition, int endPosition, ParserContext context) {
        List<ParseError> errors = new ArrayList<>();
        for (Parser<T> parseCall : parsers) {
            ParseResult<T> parseResult = parseCall.parse(startPosition, endPosition, context);

            if (parseResult.hasResult()) {
                return parseResult;
            } else {
                errors.add(parseResult.error());
            }
        }

        return ParseResult.error(name, errors.toString(), startPosition);
    }
}