/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.cycle;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.cycle.impl.connector.view.CustomizedViewConnector;

/**
 * Superclass for the composite of folders and files. Holds a reference to the
 * API used to query sub folders and files in order to enable lazy loading of
 * them.
 * 
 * @author bernd.ruecker@camunda.com
 */
public class RepositoryNode implements Serializable {

  private static final long serialVersionUID = 1L;

  protected static Logger log = Logger.getLogger(RepositoryNode.class.getName());

  /**
   * local part of the URL for node. This is the one and only used unique
   * identifier for the node used by the client and the repository API. All
   * details can be queried by this URL later on, by adding the repo base URL to
   * the beginning.
   * 
   * For Signavio the ID is simply an UUID generated by Signavio. For FileSystem
   * this is the "local" part of the absolute path of the file WITHOUT the
   * configured root folder of the FileSystem connector. For other repos this
   * may be whatever it needs to be, the client shouldn't really care.
   */
  private String id;

  // /**
  // * The URL used in the repo internally to query the artifact
  // */
  // private String sourceSystem;

  /**
   * The url used in the client (e.g. GUI) for this artifact
   */
  private String clientUrl;

  /**
   * flag to indicate if the object was queried with all details or just the
   * "header" information like the name, which required lazy loading.
   * 
   * Eager fetching or lazy loading can be decided by the Connector, since this
   * is pretty different depending on the technology.
   */
  // private boolean detailsFetched = false;

  private final RepositoryNodeMetadata metadata = new RepositoryNodeMetadata();

  private transient RepositoryConnector connector;

  private transient RepositoryConnector originalConnector;

  public RepositoryNode() {
  }

  public RepositoryNode(RepositoryConnector connector) {
    this.connector = connector;
    originalConnector = connector;
  }

  public RepositoryConnector getConnector() {
    if (connector == null) {
      throw new RepositoryException("Item " + this + " is not connected to any repository");
    }
    return connector;
  }
  
  public void overwriteConnector(RepositoryConnector connector) {
    this.connector = connector;
    // do NOT overwrite the original one
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [id=" + id + ";metadata=" + metadata + "]";
  }

  // public boolean isDetailsFetched() {
  // return detailsFetched;
  // }

  public RepositoryNodeMetadata getMetadata() {
    return metadata;
  }

  public Map<String, String> getMetadataAsMap() {
    return metadata.getAsStringMap();
  }

  public String getClientUrl() {
    return clientUrl;
  }

  public void setClientUrl(String clientUrl) {
    this.clientUrl = clientUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * get the {@link RepositoryConnector} associated with that
   * {@link ArtifactAction}. It is remembered seperately, because maybe the
   * connector is changed in the {@link RepositoryArtifact} e.g. to the
   * {@link CustomizedViewConnector}, but an action often needs to operate on
   * the original connector
   */
  public RepositoryConnector getOriginalConnector() {
    return originalConnector;
  }

  public void setOriginalConnector(RepositoryConnector originalConnector) {
    this.originalConnector = originalConnector;
  }
}
