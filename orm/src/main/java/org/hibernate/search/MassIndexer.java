/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search;

import java.util.concurrent.Future;

import org.hibernate.CacheMode;

import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;

/**
 * A MassIndexer is useful to rebuild the indexes from the
 * data contained in the database.
 * This process is expensive: all indexed entities and their
 * indexedEmbedded properties are scrolled from database.
 *
 * @author Sanne Grinovero
 */
public interface MassIndexer {

	/**
	 * Sets the number of entity types to be indexed in parallel.
	 * Defaults to 1.
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer typesToIndexInParallel(int threadsToIndexObjects);

	/**
	 * Set the number of threads to be used to load
	 * the root entities.
	 * @param numberOfThreads
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer threadsToLoadObjects(int numberOfThreads);

	/**
	 * Sets the batch size used to load the root entities.
	 * @param batchSize
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer batchSizeToLoadObjects(int batchSize);

	/**
	 * Deprecated: value is ignored.
	 * @param numberOfThreads
	 * @return <tt>this</tt> for method chaining
	 * @deprecated Being ignored: this method will be removed.
	 */
	@Deprecated
	MassIndexer threadsForSubsequentFetching(int numberOfThreads);

	/**
	 * Override the default <code>MassIndexerProgressMonitor</code>.
	 *
	 * @param monitor this instance will receive updates about the massindexing progress.
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer progressMonitor(MassIndexerProgressMonitor monitor);

	/**
	 * Sets the cache interaction mode for the data loading tasks.
	 * Defaults to <tt>CacheMode.IGNORE</tt>.
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer cacheMode(CacheMode cacheMode);

	/**
	 * If index optimization has to be started at the end
	 * of the indexing process.
	 * Defaults to <tt>true</tt>.
	 * @param optimize
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer optimizeOnFinish(boolean optimize);

	/**
	 * If index optimization should be run before starting,
	 * after the purgeAll. Has no effect if <tt>purgeAll</tt> is set to false.
	 * Defaults to <tt>true</tt>.
	 * @param optimize
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer optimizeAfterPurge(boolean optimize);

	/**
	 * If all entities should be removed from the index before starting
	 * using purgeAll. Set it to false only if you know there are no
	 * entities in the index: otherwise search results may be duplicated.
	 * Defaults to true.
	 * @param purgeAll
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer purgeAllOnStart(boolean purgeAll);

	/**
	 * EXPERIMENTAL method: will probably change
	 *
	 * Will stop indexing after having indexed a set amount of objects.
	 * As a results the index will not be consistent
	 * with the database: use only for testing on an (undefined) subset of database data.
	 * @param maximum
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer limitIndexedObjectsTo(long maximum);

	/**
	 * Starts the indexing process in background (asynchronous).
	 * Can be called only once.
	 * @return a Future to control task canceling.
	 * get() will block until completion.
	 * cancel() is currently not implemented.
	 */
	Future<?> start();

	/**
	 * Starts the indexing process, and then block until it's finished.
	 * Can be called only once.
	 * @throws InterruptedException if the current thread is interrupted
	 * while waiting.
	 */
	void startAndWait() throws InterruptedException;

	/**
	 * Specifies the fetch size to be used when loading primary keys
	 * if objects to be indexed. Some databases accept special values,
	 * for example MySQL might benefit from using {@link Integer#MIN_VALUE}
	 * otherwise it will attempt to preload everything in memory.
	 * @param idFetchSize
	 * @return <tt>this</tt> for method chaining
	 */
	MassIndexer idFetchSize(int idFetchSize);

}
