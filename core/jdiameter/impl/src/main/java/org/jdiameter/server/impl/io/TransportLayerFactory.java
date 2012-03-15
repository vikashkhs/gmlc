package org.jdiameter.server.impl.io;

import static java.lang.Class.forName;

import java.lang.reflect.Constructor;
import java.net.InetAddress;

import org.jdiameter.api.Configuration;
import org.jdiameter.client.api.io.TransportError;
import org.jdiameter.client.api.io.TransportException;
import org.jdiameter.client.api.parser.IMessageParser;
import org.jdiameter.client.impl.helpers.AppConfiguration;
import org.jdiameter.common.api.concurrent.IConcurrentFactory;
import org.jdiameter.server.api.IMetaData;
import org.jdiameter.server.api.io.INetworkConnectionListener;
import org.jdiameter.server.api.io.INetworkGuard;
import org.jdiameter.server.api.io.ITransportLayerFactory;
import org.jdiameter.server.impl.helpers.ExtensionPoint;
import org.jdiameter.server.impl.helpers.Parameters;

public class TransportLayerFactory extends org.jdiameter.client.impl.transport.TransportLayerFactory implements ITransportLayerFactory {

	private final IConcurrentFactory concurrentFactory;
	private final IMetaData metaData;
	private Class<INetworkGuard> networkGuardClass;
	private Constructor<INetworkGuard> networkGuardConstructor;

	public TransportLayerFactory(Configuration conf, IConcurrentFactory concurrentFactory, IMessageParser parser, IMetaData metaData)
			throws TransportException {
		super(conf, parser);

		this.concurrentFactory = concurrentFactory;
		this.metaData = metaData;
		String networkGuardClassName = null;
		Configuration[] children = config.getChildren(Parameters.Extensions.ordinal());
		// extract network guard class name.
		AppConfiguration internalExtensions = (AppConfiguration) children[ExtensionPoint.Internal.id()];
		networkGuardClassName = internalExtensions.getStringValue(ExtensionPoint.InternalNetworkGuard.ordinal(),
				(String) ExtensionPoint.InternalNetworkGuard.defValue());

		try {
			// TODO: this should be enough to check if class has interface!?
			this.networkGuardClass = (Class<INetworkGuard>) forName(networkGuardClassName);

			if (!INetworkGuard.class.isAssignableFrom(this.networkGuardClass))
				throw new TransportException("Specified class does not inherit INetworkGuard interface " + this.networkGuardClass,
						TransportError.Internal);
		} catch (Exception e) {
			throw new TransportException("Cannot prepare specified guard class " + this.networkGuardClass, TransportError.Internal, e);
		}

		try {
			// TODO: this is bad practice, IConnection is interface and this code enforces constructor type to be present!
			networkGuardConstructor = this.networkGuardClass.getConstructor(InetAddress.class, Integer.TYPE, IConcurrentFactory.class,
					IMessageParser.class, IMetaData.class);

		} catch (Exception e) {
			throw new TransportException("Cannot find required constructor", TransportError.Internal, e);
		}
	}

	public INetworkGuard createNetworkGuard(InetAddress inetAddress, int port) throws TransportException {
		try {
			return networkGuardConstructor.newInstance(inetAddress, port, this.concurrentFactory, this.parser, this.metaData);
		} catch (Exception e) {
			throw new TransportException(TransportError.NetWorkError, e);
		}
	}

	public INetworkGuard createNetworkGuard(InetAddress inetAddress, final int port, final INetworkConnectionListener listener)
			throws TransportException {
		INetworkGuard guard;
		try {
			guard = networkGuardConstructor.newInstance(inetAddress, port, this.concurrentFactory, this.parser, this.metaData);
		} catch (Exception e) {
			throw new TransportException(TransportError.NetWorkError, e);
		}
		guard.addListener(listener);
		return guard;
	}
}
