package org.basex.api.webdav;

import static org.basex.api.webdav.BXResourceFactory.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.basex.core.cmd.Open;
import org.basex.server.ClientQuery;
import org.basex.server.ClientSession;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.FolderResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Resource;

/**
 * WebDAV resource representing a folder within a collection database.
 * @author BaseX Team 2005-11, BSD License
 * @author Rositsa Shadura
 * @author Dimitar Popov
 */
public class BXFolder extends BXResource implements FolderResource {
  /** Database name. */
  private final String db;
  /** Path to folder. */
  private final String path;

  /**
   * Constructor.
   * @param dbname database name
   * @param folderPath path to folder
   */
  public BXFolder(final String dbname, final String folderPath) {
    db = dbname;
    path = folderPath;
  }

  /**
   * Constructor.
   * @param dbname database name
   * @param folderPath path to folder
   * @param u user name
   * @param p password
   */
  public BXFolder(final String dbname, final String folderPath, final String u,
      final String p) {
    db = dbname;
    path = folderPath;
    user = u;
    pass = p;
  }

  @Override
  public CollectionResource createCollection(final String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Resource child(final String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<? extends Resource> getChildren() {
    final List<BXResource> ch = new ArrayList<BXResource>();
    final List<String> paths = new ArrayList<String>();
    try {
      final ClientSession cs = login(user, pass);
      try {
        // List children of this folder
        ClientQuery q = cs.query("collection('" + db + "')" +
            "/.[starts-with(doc-name(),'" + path + "')]" +
            "/substring-after(doc-name(),'" + path + "/')");
        while(q.more()) {
          final String nextPath = q.next();
          final int sepIdx = nextPath.indexOf(DIRSEP);
          // Document
          if(sepIdx <= 0) ch.add(new BXDocument(db, path + DIRSEP + nextPath,
              user, pass));
          else {
            // Folder
            final String folderName = nextPath.substring(0, sepIdx);
            final String folderPath = path + DIRSEP + folderName;
            if(!paths.contains(folderPath)) paths.add(folderPath);
          }
        }
        for(final String f : paths)
          ch.add(new BXFolder(db, f, user, pass));
      } finally {
        cs.close();
      }
    } catch(Exception e) {
      // [RS] WebDAV: error handling
      e.printStackTrace();
    }
    return ch;
  }

  @Override
  public Date getModifiedDate() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName() {
    final int idx = path.lastIndexOf(DIRSEP);
    return path.substring(idx + 1, path.length());
  }

  @Override
  public Resource createNew(final String arg0, final InputStream arg1,
      final Long arg2, final String arg3) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void copyTo(final CollectionResource arg0, final String arg1) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete() {
    try {
      ClientSession cs = login(user, pass);
      try {
        // Open database
        cs.execute(new Open(db));
        // Delete folder from database
        cs.query("db:delete('" + path + "')").execute();
      } finally {
        cs.close();
      }
    } catch(Exception e) {
      // [RS] WebDAV: error handling
      e.printStackTrace();
    }
  }

  @Override
  public Long getContentLength() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getContentType(final String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long getMaxAgeSeconds(final Auth arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void sendContent(final OutputStream arg0, final Range arg1,
      final Map<String, String> arg2, final String arg3) {
    // TODO Auto-generated method stub
  }

  @Override
  public void moveTo(final CollectionResource arg0, final String arg1) {
    // TODO Auto-generated method stub
  }

  @Override
  public Date getCreateDate() {
    // TODO Auto-generated method stub
    return null;
  }
}
