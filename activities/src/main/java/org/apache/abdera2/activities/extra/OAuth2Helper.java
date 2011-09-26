package org.apache.abdera2.activities.extra;
import java.io.IOException;

import org.apache.abdera2.activities.model.ASBase;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.iri.IRI;
import org.apache.abdera2.common.mediatype.MimeTypeHelper;
import org.apache.abdera2.common.templates.MapContext;
import org.apache.abdera2.common.templates.QueryContext;
import org.apache.abdera2.protocol.client.BasicClient;
import org.apache.abdera2.protocol.client.Client;
import org.apache.abdera2.protocol.client.ClientResponse;
import org.apache.abdera2.protocol.client.RequestOptions;
import org.apache.abdera2.protocol.client.Session;
import org.apache.http.entity.StringEntity;

/**
 * Helper class for using OAuth2 designed to make it easier to implement
 * client side OAuth2 operations.
 */
public class OAuth2Helper {
  
  public static String getAuthTokenRequestUri(
    String base,
    String clientid,
    String redirectUrl,
    String... scopes) {
      MapContext context = 
        new QueryContext(base);
      context.put(
        "client_id", 
        clientid);
      context.put(
        "redirect_uri", 
        redirectUrl);
      context.put(
        "scopes", 
        scopes);
      return QueryContext
        .templateFromIri(
          new IRI(base),
          context)
        .expand(context);   
  }
  
  public static String extractAuthorizationCode(String iri) {
    MapContext context = 
      new QueryContext(iri);
    return (String)context.get("code");
  }
  
  public static String getAccessTokenRequestPayload(
    String clientid,
    String secret,
    String redirectUrl,
    String code) {
    MapContext context = 
      new MapContext();
    context.put(
      "client_id", 
      clientid);
    context.put(
      "redirect_uri", 
      redirectUrl);
    context.put(
      "client_secret", 
      secret);
    context.put(
      "code", 
      code);
    context.put(
      "grant_type", 
      "authorization_code");
    String post = 
      QueryContext
        .templateFromContext(
          context, 
          false)
           .expand(context);
    return post.substring(1);
  }
  
  public static String getRefreshTokenRequestPayload(
      String clientid,
      String secret,
      String refreshToken,
      String code) {
      MapContext context = 
        new MapContext();
      context.put(
        "client_id", 
        clientid);
      context.put(
        "refresh_token", 
        refreshToken);
      context.put(
        "client_secret", 
        secret);
      context.put(
        "grant_type", 
        "refresh_token");
      String post = 
        QueryContext
          .templateFromContext(
            context, 
            false)
             .expand(context);
      return post.substring(1);
  }
  
  public static ASBase getToken(String uri, String payload) throws IOException {
    return getToken(new BasicClient(), uri,payload);
  }
  
  public static ASBase getToken(Client client, String uri, String payload) throws IOException {
    return getToken(client.newSession(),uri,payload);
  }
  
  public static ASBase getToken(Session session, String uri, String payload) throws IOException {
    ASBase token = null;
    RequestOptions options = 
      session.getDefaultRequestOptions();
    options.setContentType("application/x-www-form-urlencoded");
    ClientResponse resp = 
      session.post(
        uri, 
        new StringEntity(payload),
        options);
    if (resp.getContentType() != null && 
        MimeTypeHelper.isJson(resp.getContentType().toString())) {
      IO io = IO.get();
      token = io.read(resp.getInputStream(), "UTF-8");
    }
    return token;
  }
}
