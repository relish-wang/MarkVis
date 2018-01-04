package com.hustoj.network;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author zhouqian
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class X509TrustManagerBuilder {

  private static final String TAG = "X509TrustManagerBuilder";

  public static SSLSocketFactory createSSLSocketFactory(TrustManager... trustManager) {
    try {
      final SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustManager, null);
      return sslContext.getSocketFactory();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * If the <code>trustManagers</code>'s length is 1 and the only element is instance of
   * {@link X509TrustManager}, returns the element directly. Otherwise, wrap it into
   * {@link X509TrustManagerImpl} instance.
   *
   * @param trustManagers null or empty to trust any certificates.
   * @return the {@link X509TrustManager} instance
   */
  public static X509TrustManager toX509TrustManager(final TrustManager... trustManagers) {
    if (trustManagers.length == 1 && trustManagers[0] instanceof X509TrustManager) {
      return (X509TrustManager) trustManagers[0];
    } else {
      return new X509TrustManagerImpl(new DelegatedTrustManager() {
        @Override public TrustManager[] build() {
          return trustManagers;
        }
      });
    }
  }

  /**
   * Use {@link TrustManagerFactory#getDefaultAlgorithm()} algorithm to create a
   * {@link TrustManagerFactory} instance, which trusts <code>keyStore</code>.
   *
   * @param keyStore the keystore to trust, pass null to trust what system trust
   * @return the {@link TrustManagerFactory} instance
   * @throws KeyStoreException if the keystore has something wrong
   */
  private static TrustManagerFactory trustManagerFactory(KeyStore keyStore)
      throws KeyStoreException {
    try {
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
          TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);
      return trustManagerFactory;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("using default algorithm, should never happens", e);
    }
  }


  /**
   * returns null or empty to trust any certificate;
   * returns nonempty to trust specified certificates.
   */
  private interface DelegatedTrustManager {
    TrustManager[] build();
  }

  /**
   * Delegate implement of {@link X509TrustManager}. The only constructor is
   * {@link #X509TrustManagerImpl(DelegatedTrustManager)}.
   *
   * @see DelegatedTrustManager
   */
  private static final class X509TrustManagerImpl implements X509TrustManager {

    private DelegatedTrustManager mDelegatedTrustManager;

    // lazily initialize
    private X509TrustManager[] mX509TrustManagers;
    private X509Certificate[] mX509Certificates;

    public X509TrustManagerImpl(DelegatedTrustManager delegatedTrustManager) {
      if (delegatedTrustManager == null) throw new NullPointerException();
      mDelegatedTrustManager = delegatedTrustManager;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
      confirmInflated();
      for (X509TrustManager manager : mX509TrustManagers) {
        try {
          manager.checkClientTrusted(chain, authType);
          return;
        } catch (CertificateException ignore) {
        }
      }
      if (mX509TrustManagers.length > 0) {
        throw new CertificateException();
      }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
      confirmInflated();
      for (X509TrustManager manager : mX509TrustManagers) {
        try {
          manager.checkServerTrusted(chain, authType);
          return;
        } catch (CertificateException ignore) {
        }
      }
      if (mX509TrustManagers.length > 0) {
        throw new CertificateException();
      }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      confirmInflated();
      return mX509Certificates;
    }

    private void confirmInflated() {
      if (mDelegatedTrustManager != null) {
        synchronized (this) {
          if (mDelegatedTrustManager != null) {
            List<X509TrustManager> x509TrustManagerList = new ArrayList<>();
            List<X509Certificate> x509CertificateList = new ArrayList<>();

            TrustManager[] trustManagers = mDelegatedTrustManager.build();

            if (trustManagers != null) {
              for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                  final X509TrustManager tm = (X509TrustManager) trustManager;
                  x509TrustManagerList.add(tm);

                  final X509Certificate[] certs = tm.getAcceptedIssuers();
                  if (certs != null && certs.length > 0) {
                    x509CertificateList.addAll(Arrays.asList(certs));
                  }
                } else {
                  if (trustManager != null) {
                    Log.e(TAG, "trustManager not instance of X509TrustManager: "
                        + trustManager.getClass().getName());
                  }
                }
              }
            }

            mX509TrustManagers = x509TrustManagerList.toArray(
                new X509TrustManager[x509TrustManagerList.size()]
            );
            mX509Certificates = x509CertificateList.toArray(
                new X509Certificate[x509CertificateList.size()]
            );
            mDelegatedTrustManager = null;
          }
        }
      }
    }
  }


  /* package */ boolean trustSystemTrust = false;
  /* package */ final List<byte[]> encodedCerts = new ArrayList<>();

  public X509TrustManagerBuilder trustWhatSystemTrust() {
    trustSystemTrust = true;
    return this;
  }

  public X509TrustManagerBuilder trustSpecifiedCertificate(byte[] encodedCert) {
    encodedCerts.add(encodedCert);
    return this;
  }

  public X509TrustManager build() {
    final X509TrustManager systemTrustManager;
    if (trustSystemTrust) {
      try {
        final TrustManager[] trustManagers = trustManagerFactory(null).getTrustManagers();
        systemTrustManager = toX509TrustManager(trustManagers);
      } catch (KeyStoreException e) {
        throw new RuntimeException("Can't find system trust manager", e);
      }
    } else {
      systemTrustManager = null;
    }

    final X509TrustManager certTrustManager;
    if (encodedCerts.size() > 0) {
      try {
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);

        for (int i = 0, size = encodedCerts.size(); i < size; ++i) {
          final InputStream certificateSteam = new ByteArrayInputStream(encodedCerts.get(i));
          final String certificateAlias = "X.509-" + String.valueOf(i);
          keyStore.setCertificateEntry(certificateAlias,
              certificateFactory.generateCertificate(certificateSteam));
        }
        final TrustManager[] trustManagers = trustManagerFactory(keyStore).getTrustManagers();

        certTrustManager = toX509TrustManager(trustManagers);
      } catch (Exception e) {
        throw new RuntimeException("Specified certificate error", e);
      }
    } else {
      certTrustManager = null;
    }

    if (systemTrustManager != null && certTrustManager == null) {
      return systemTrustManager;
    } else if (systemTrustManager == null && certTrustManager != null) {
      return certTrustManager;
    } else {
      return toX509TrustManager(systemTrustManager, certTrustManager);
    }
  }

}
