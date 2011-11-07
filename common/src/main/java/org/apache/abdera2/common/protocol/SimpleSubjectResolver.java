/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.common.protocol;

import java.io.Serializable;
import java.security.Principal;

import javax.security.auth.Subject;

import org.apache.abdera2.common.misc.MoreFunctions;

import com.google.common.base.Function;

/**
 * The default subject resolver implementation
 */
public class SimpleSubjectResolver 
  implements Function<Request,Subject> {

    public static final Principal ANONYMOUS = 
      new AnonymousPrincipal();

    public Subject apply(Request request) {
        RequestContext context = (RequestContext)request;
        return apply(context.getPrincipal());
    }

    public Subject apply(Principal principal) {
        Subject subject = new Subject();
        subject.getPrincipals().add((principal != null) ? principal : ANONYMOUS);
        return subject;
    }

    public Subject apply(String userid) {
        if (userid == null)
            return apply(ANONYMOUS);
        return apply(new SimplePrincipal(userid));
    }

    static class SimplePrincipal implements Principal, Serializable {
        private static final long serialVersionUID = 7161420960293729670L;
        final String name;

        SimplePrincipal(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
          return MoreFunctions.genHashCode(1, name);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final SimplePrincipal other = (SimplePrincipal)obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

    }

    public static final class AnonymousPrincipal implements Principal, Serializable {
        private static final long serialVersionUID = -5050930075733261944L;
        final String name = "Anonymous";

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }

        public boolean equals(Object other) {
            if (other == null)
                return false;
            return this == other;
        }

        public int hashCode() {
          return MoreFunctions.genHashCode(1, name);
        }
    }
}
