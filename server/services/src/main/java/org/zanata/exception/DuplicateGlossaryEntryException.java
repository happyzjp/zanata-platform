/*
 * Copyright 2015, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */

package org.zanata.exception;

import org.zanata.common.LocaleId;

/**
 * Exception for when HGlossaryEntry have duplication with same source content,
 * pos and description.
 *
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */
public class DuplicateGlossaryEntryException extends RuntimeException {

    private static final long serialVersionUID = -7974089324141736891L;

    public DuplicateGlossaryEntryException(LocaleId srcLocale,
            String sourceContent, String pos, String description) {
        super("Duplicate glossary entry in locale:" + srcLocale
                + " ,content:" + sourceContent + " ,pos:"
                + pos + " ,description:"
                + description);
    }
}
