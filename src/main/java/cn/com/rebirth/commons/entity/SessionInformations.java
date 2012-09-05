/**
* Copyright (c) 2005-2011 www.china-cti.com
* Id: SessionInformations.java 2011-5-11 11:53:02 l.xue.nong$$
*/
package cn.com.rebirth.commons.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

/**
 * The Class SessionInformations.
 *
 * @author l.xue.nong
 */
public class SessionInformations implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4042981177439614255L;

	/** The Constant SESSION_COUNTRY_KEY. */
	public static final String SESSION_COUNTRY_KEY = "rebirth.country";

	/** The Constant SESSION_REMOTE_ADDR. */
	public static final String SESSION_REMOTE_ADDR = "rebirth.remoteAddr";

	/** The Constant SESSION_REMOTE_USER. */
	public static final String SESSION_REMOTE_USER = "rebirth.remoteUser";
	/** The Constant TEMP_OUTPUT. */
	private static final ByteArrayOutputStream TEMP_OUTPUT = new ByteArrayOutputStream(8 * 1024);

	/** The id. */
	private String id;

	/** The last access. */
	private Date lastAccess;

	/** The age. */
	private Date age;

	/** The expiration date. */
	private Date expirationDate;

	/** The attribute count. */
	private int attributeCount;

	/** The serializable. */
	private boolean serializable;

	/** The country. */
	private String country;

	/** The remote addr. */
	private String remoteAddr;

	/** The  user. */
	private String remoteUser;

	/** The serialized size. */
	private int serializedSize;

	/** The attributes. */
	private List<SessionAttribute> attributes;

	/**
	 * The Class SessionAttribute.
	 *
	 * @author l.xue.nong
	 */
	public static class SessionAttribute implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 4786854834871331127L;

		/** The name. */
		private final String name;

		/** The type. */
		private final String type;

		/** The content. */
		private final String content;

		/** The serializable. */
		private final boolean serializable;

		/** The serialized size. */
		private final int serializedSize;

		/**
		 * Instantiates a new session attribute.
		 *
		 * @param session the session
		 * @param attributeName the attribute name
		 */
		public SessionAttribute(HttpSession session, String attributeName) {
			super();
			assert session != null;
			assert attributeName != null;
			name = attributeName;
			final Object value = session.getAttribute(attributeName);
			serializable = value == null || value instanceof Serializable;
			serializedSize = getObjectSize(value);
			if (value == null) {
				content = null;
				type = null;
			} else {
				String tmp;
				try {
					tmp = String.valueOf(value);
				} catch (final Exception e) {
					tmp = e.toString();
				}
				content = tmp;
				type = value.getClass().getName();
			}
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		String getName() {
			return name;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		String getType() {
			return type;
		}

		/**
		 * Gets the content.
		 *
		 * @return the content
		 */
		String getContent() {
			return content;
		}

		/**
		 * Checks if is serializable.
		 *
		 * @return true, if is serializable
		 */
		boolean isSerializable() {
			return serializable;
		}

		/**
		 * Gets the serialized size.
		 *
		 * @return the serialized size
		 */
		int getSerializedSize() {
			return serializedSize;
		}
	}

	public SessionInformations() {
	}

	/**
	 * Instantiates a new session informations.
	 *
	 * @param session the session
	 * @param includeAttributes the include attributes
	 */
	@SuppressWarnings("unchecked")
	public SessionInformations(HttpSession session, boolean includeAttributes) {
		super();
		assert session != null;
		id = session.getId();
		final long now = System.currentTimeMillis();
		lastAccess = new Date(now - session.getLastAccessedTime());
		age = new Date(now - session.getCreationTime());
		expirationDate = new Date(session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L);

		final List<String> attributeNames = Collections.list(session.getAttributeNames());
		attributeCount = attributeNames.size();
		serializable = computeSerializable(session, attributeNames);

		final Object countryCode = session.getAttribute(SESSION_COUNTRY_KEY);
		if (countryCode == null) {
			country = null;
		} else {
			country = countryCode.toString().toLowerCase(Locale.getDefault());
		}

		final Object addr = session.getAttribute(SESSION_REMOTE_ADDR);
		if (addr == null) {
			remoteAddr = null;
		} else {
			remoteAddr = addr.toString();
		}

		final Object user = session.getAttribute(SESSION_REMOTE_USER);
		if (user == null) {
			remoteUser = null;
		} else {
			remoteUser = user.toString();
		}

		serializedSize = computeSerializedSize(session, attributeNames);

		if (includeAttributes) {
			attributes = new ArrayList<SessionAttribute>(attributeCount);
			for (final String attributeName : attributeNames) {
				attributes.add(new SessionAttribute(session, attributeName));
			}
		} else {
			attributes = null;
		}
	}

	/**
	 * Compute serializable.
	 *
	 * @param session the session
	 * @param attributeNames the attribute names
	 * @return true, if successful
	 */
	private boolean computeSerializable(HttpSession session, List<String> attributeNames) {
		for (final String attributeName : attributeNames) {
			final Object attributeValue = session.getAttribute(attributeName);
			if (!(attributeValue == null || attributeValue instanceof Serializable)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Compute serialized size.
	 *
	 * @param session the session
	 * @param attributeNames the attribute names
	 * @return the int
	 */
	private int computeSerializedSize(HttpSession session, List<String> attributeNames) {
		if (!serializable) {
			// la taille pour la session est inconnue si un de ses attributs n'est pas sérialisable
			return -1;
		}
		final List<Serializable> serializableAttributes = new ArrayList<Serializable>(attributeNames.size());
		for (final String attributeName : attributeNames) {
			final Object attributeValue = session.getAttribute(attributeName);
			serializableAttributes.add((Serializable) attributeValue);
		}
		return getObjectSize(serializableAttributes);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the last access.
	 *
	 * @return the last access
	 */
	public Date getLastAccess() {
		return lastAccess;
	}

	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public Date getAge() {
		return age;
	}

	/**
	 * Gets the expiration date.
	 *
	 * @return the expiration date
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Gets the attribute count.
	 *
	 * @return the attribute count
	 */
	public int getAttributeCount() {
		return attributeCount;
	}

	/**
	 * Checks if is serializable.
	 *
	 * @return true, if is serializable
	 */
	public boolean isSerializable() {
		return serializable;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Gets the country display.
	 *
	 * @return the country display
	 */
	public String getCountryDisplay() {
		final String myCountry = getCountry();
		if (myCountry == null) {
			return null;
		}
		// "fr" est sans conséquence
		return new Locale("fr", myCountry).getDisplayCountry(Locale.getDefault());
	}

	/**
	 * Gets the remote addr.
	 *
	 * @return the remote addr
	 */
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * Gets the remote user.
	 *
	 * @return the remote user
	 */
	public String getRemoteUser() {
		return remoteUser;
	}

	/**
	 * Gets the serialized size.
	 *
	 * @return the serialized size
	 */
	public int getSerializedSize() {
		return serializedSize;
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<SessionAttribute> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + getId() + ", remoteAddr=" + getRemoteAddr() + ", serializedSize="
				+ getSerializedSize() + ']';
	}

	/**
	 * Gets the object size.
	 *
	 * @param object the object
	 * @return the object size
	 */
	public static int getObjectSize(Object object) {
		if (!(object instanceof Serializable)) {
			return -1;
		}
		final Serializable serializable = (Serializable) object;
		synchronized (TEMP_OUTPUT) {
			TEMP_OUTPUT.reset();
			try {
				final ObjectOutputStream out = new ObjectOutputStream(TEMP_OUTPUT);
				try {
					out.writeObject(serializable);
				} finally {
					out.close();
				}
				return TEMP_OUTPUT.size();
			} catch (final IOException e) {
				return -1;
			}
		}
	}

}
