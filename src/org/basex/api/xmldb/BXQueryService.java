package org.basex.api.xmldb;

import org.basex.BaseX;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.xpath.XPathProcessor;
import org.basex.query.xquery.XQueryProcessor;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

/**
 * Abstract QueryService definition for the XMLDB:API.
 * 
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Andreas Weiler
 */
public final class BXQueryService implements XPathQueryService {
  /** XPath service constant. */
  public static final String XPATH = "XPathQueryService";
  /** XQuery service constant. */
  public static final String XQUERY = "XQueryQueryService";
  /** Service name. */
  private final String name;
  /** Collection reference. */
  private BXCollection coll;

  /**
   * Standard constructor.
   * @param c for collection reference
   * @param n service name
   */
  public BXQueryService(final BXCollection c, final String n) {
    coll = c;
    name = n;
  }

  public void clearNamespaces() {
    BaseX.notimplemented();
  }

  public String getName() {
    return name;
  }

  public String getNamespace(final String prefix) {
    BaseX.notimplemented();
    return null;
  }

  public String getProperty(final String name) {
    BaseX.notimplemented();
    return null;
  }

  public String getVersion() {
    return "1.0";
  }

  public ResourceSet query(final String query) throws XMLDBException {
    try {
      // Creates a query instance
      final QueryProcessor proc = name.equals(XPATH) ?
          new XPathProcessor(query) : new XQueryProcessor(query);
      // Executes the query and returns the result
      return new BXResourceSet(proc.query(coll.ctx.current()));
    } catch(final QueryException ex) {
      throw new XMLDBException(ErrorCodes.VENDOR_ERROR, ex.getMessage());
    } catch(final Exception ex) {
      BaseX.notexpected();
      return null;
    }
  }

  public ResourceSet queryResource(final String id, final String query) {
    BaseX.notimplemented();
    return null;
  }

  public void removeNamespace(final String prefix) {
    BaseX.notimplemented();
  }

  public void setCollection(final Collection col) {
    coll = (BXCollection) col;
  }

  public void setNamespace(final String prefix, final String uri) {
    BaseX.notimplemented();
  }

  public void setProperty(final String name, final String value) {
    BaseX.notimplemented();
  }
}