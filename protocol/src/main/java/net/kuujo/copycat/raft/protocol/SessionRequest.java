/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.raft.protocol;

import net.kuujo.copycat.util.BuilderPool;
import net.kuujo.copycat.io.BufferInput;
import net.kuujo.copycat.io.BufferOutput;
import net.kuujo.copycat.io.serializer.Serializer;
import net.kuujo.copycat.util.ReferenceFactory;
import net.kuujo.copycat.util.ReferenceManager;

/**
 * Session request.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public abstract class SessionRequest<T extends SessionRequest<T>> extends ClientRequest<T> {
  protected long session;

  public SessionRequest(ReferenceManager<T> referenceManager) {
    super(referenceManager);
  }

  /**
   * Returns the session ID.
   *
   * @return The session ID.
   */
  public long session() {
    return session;
  }

  @Override
  public void readObject(BufferInput buffer, Serializer serializer) {
    super.readObject(buffer, serializer);
    session = buffer.readLong();
  }

  @Override
  public void writeObject(BufferOutput buffer, Serializer serializer) {
    super.writeObject(buffer, serializer);
    buffer.writeLong(session);
  }

  /**
   * Client request builder.
   */
  public static abstract class Builder<T extends Builder<T, U>, U extends SessionRequest<U>> extends ClientRequest.Builder<T, U> {

    protected Builder(BuilderPool<T, U> pool, ReferenceFactory<U> factory) {
      super(pool, factory);
    }

    @Override
    protected void reset() {
      super.reset();
      request.session = 0;
    }

    /**
     * Sets the session ID.
     *
     * @param session The session ID.
     * @return The request builder.
     */
    @SuppressWarnings("unchecked")
    public T withSession(long session) {
      if (session <= 0)
        throw new IllegalArgumentException("session must be positive");
      request.session = session;
      return (T) this;
    }
  }

}
