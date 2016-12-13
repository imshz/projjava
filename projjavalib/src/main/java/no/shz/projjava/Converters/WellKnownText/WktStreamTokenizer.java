package no.shz.projjava.converters.wellKnownText;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import no.shz.projjava.converters.wellKnownText.io.StreamTokenizer;
import no.shz.utilities.Ref;
import no.shz.utilities.TextReader;

/**
 * Copyright 2005 - 2009 - Morten Nielsen (www.sharpgis.net)
 * This file is part of ProjNet.
 * ProjNet is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjNet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ProjNet; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * SOURCECODE IS MODIFIED FROM ANOTHER WORK AND IS ORIGINALLY BASED ON GeoTools.NET:
 *
 *  Copyright (C) 2002 Urban Science Applications, Inc. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

/**
 * ProjJava - ported by imshz (https://github.com/imshz/projjava)
 *
 * ProjJava is a android compatible point-to-point coordinate conversions java library.
 *
 * This library is a port of Proj.NET - ProjNet is free software published under
 * the terms of GNU Lesser General Public License, as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProjJava is published under the same license and will allways follow the same
 * restrictions as the original project.
 */

/**
 * Reads a stream of Well Known Text (wkt) string and returns a stream of tokens.
 */
class WktStreamTokenizer extends StreamTokenizer {

    /**
     * Initializes a new instance of the WktStreamTokenizer class.
     * The WktStreamTokenizer class ais in reading WKT streams.
     *
     * @param reader A TextReader that contains
     */
    WktStreamTokenizer(TextReader reader) {
        super(reader, true);
    }

    /**
     * Reads a token and checks it is what is expected.
     *
     * @param expectedToken The expected token.
     */
    final void readToken(String expectedToken) throws IOException {
        this.nextToken();
        if (!this.getStringValue().equals(expectedToken)) {
            throw new IllegalArgumentException(String.format(Locale.US, "Expecting ('%4$s') but got a '%1$s' at line %2$s column %3$s.", this.getStringValue(), this.getLineNumber(), this.getColumn(), expectedToken));
        }
    }

    /**
     * Reads a string inside double quotes.
     * White space inside quotes is preserved.
     *
     * @return The string inside the double quotes.
     */
    final String readDoubleQuotedWord() throws IOException {
        String word = "";
        readToken("\"");
        nextToken(false);
        while (!getStringValue().equals("\"")) {
            word = word + this.getStringValue();
            nextToken(false);
        }
        return word;
    }

    /**
     * Reads the authority and authority code.
     *
     * @param authority     String to place the authority in.
     * @param authorityCode String to place the authority code in.
     */
    final void readAuthority(Ref<String> authority, Ref<Long> authorityCode) throws IOException {
        if (!getStringValue().equals("AUTHORITY")) {
            readToken("AUTHORITY");
        }
        readToken("[");
        authority.value = this.readDoubleQuotedWord();
        readToken(",");

        try {
            authorityCode.value = NumberFormat.getNumberInstance().parse(this.readDoubleQuotedWord()).longValue();
        } catch (Exception ignored) {
        }
        readToken("]");
    }
}