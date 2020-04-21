/**
 * 
 */
package com.citusdata.migration.compatibility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateBackupRequest;
import com.amazonaws.services.dynamodbv2.model.CreateBackupResult;
import com.amazonaws.services.dynamodbv2.model.CreateGlobalTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateGlobalTableResult;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteBackupRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteBackupResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeBackupRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeBackupResult;
import com.amazonaws.services.dynamodbv2.model.DescribeContinuousBackupsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeContinuousBackupsResult;
import com.amazonaws.services.dynamodbv2.model.DescribeEndpointsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeEndpointsResult;
import com.amazonaws.services.dynamodbv2.model.DescribeGlobalTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeGlobalTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeGlobalTableSettingsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeGlobalTableSettingsResult;
import com.amazonaws.services.dynamodbv2.model.DescribeLimitsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeLimitsResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.ListBackupsRequest;
import com.amazonaws.services.dynamodbv2.model.ListBackupsResult;
import com.amazonaws.services.dynamodbv2.model.ListGlobalTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListGlobalTablesResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ListTagsOfResourceRequest;
import com.amazonaws.services.dynamodbv2.model.ListTagsOfResourceResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.RestoreTableFromBackupRequest;
import com.amazonaws.services.dynamodbv2.model.RestoreTableFromBackupResult;
import com.amazonaws.services.dynamodbv2.model.RestoreTableToPointInTimeRequest;
import com.amazonaws.services.dynamodbv2.model.RestoreTableToPointInTimeResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TagResourceRequest;
import com.amazonaws.services.dynamodbv2.model.TagResourceResult;
import com.amazonaws.services.dynamodbv2.model.UntagResourceRequest;
import com.amazonaws.services.dynamodbv2.model.UntagResourceResult;
import com.amazonaws.services.dynamodbv2.model.UpdateContinuousBackupsRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateContinuousBackupsResult;
import com.amazonaws.services.dynamodbv2.model.UpdateGlobalTableRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateGlobalTableResult;
import com.amazonaws.services.dynamodbv2.model.UpdateGlobalTableSettingsRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateGlobalTableSettingsResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTableResult;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import com.citusdata.migration.datamodel.TableSchema;

/**
 * @author marco
 *
 */
public class PostgresDynamoDB implements AmazonDynamoDB {

	final Connection connection;
	final Map<String,TableSchema> schemaCache;
	
	public PostgresDynamoDB(String postgresJDBCURL) throws SQLException {
		this.connection = DriverManager.getConnection(postgresJDBCURL);
		this.schemaCache = new HashMap<>();
	}

	@Override
	public void setEndpoint(String endpoint) {
	}

	@Override
	public void setRegion(Region region) {
		
		
	}

	@Override
	public BatchGetItemResult batchGetItem(BatchGetItemRequest batchGetItemRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchWriteItemResult batchWriteItem(BatchWriteItemRequest batchWriteItemRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchWriteItemResult batchWriteItem(Map<String, List<WriteRequest>> requestItems) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateTableResult createTable(CreateTableRequest createTableRequest) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public CreateTableResult createTable(List<AttributeDefinition> attributeDefinitions, String tableName,
			List<KeySchemaElement> keySchema, ProvisionedThroughput provisionedThroughput) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteItemResult deleteItem(DeleteItemRequest deleteItemRequest) {
		
		return null;
	}

	@Override
	public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteTableResult deleteTable(DeleteTableRequest deleteTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteTableResult deleteTable(String tableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeLimitsResult describeLimits(DescribeLimitsRequest describeLimitsRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTableResult describeTable(DescribeTableRequest describeTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTableResult describeTable(String tableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTimeToLiveResult describeTimeToLive(DescribeTimeToLiveRequest describeTimeToLiveRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(GetItemRequest getItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(String tableName, Map<String, AttributeValue> key) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(String tableName, Map<String, AttributeValue> key, Boolean consistentRead) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(ListTablesRequest listTablesRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables() {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(String exclusiveStartTableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(String exclusiveStartTableName, Integer limit) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(Integer limit) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTagsOfResourceResult listTagsOfResource(ListTagsOfResourceRequest listTagsOfResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(PutItemRequest putItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(String tableName, Map<String, AttributeValue> item) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(String tableName, Map<String, AttributeValue> item, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public QueryResult query(QueryRequest queryRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(ScanRequest scanRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, List<String> attributesToGet) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, Map<String, Condition> scanFilter) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public TagResourceResult tagResource(TagResourceRequest tagResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UntagResourceResult untagResource(UntagResourceRequest untagResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(UpdateItemRequest updateItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(
			String tableName,
			Map<String, AttributeValue> key,
			Map<String, AttributeValueUpdate> attributeUpdates) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key,
			Map<String, AttributeValueUpdate> attributeUpdates, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTableResult updateTable(UpdateTableRequest updateTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTableResult updateTable(String tableName, ProvisionedThroughput provisionedThroughput) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTimeToLiveResult updateTimeToLive(UpdateTimeToLiveRequest updateTimeToLiveRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	@Override
	public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public AmazonDynamoDBWaiters waiters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateBackupResult createBackup(CreateBackupRequest createBackupRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateGlobalTableResult createGlobalTable(CreateGlobalTableRequest createGlobalTableRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteBackupResult deleteBackup(DeleteBackupRequest deleteBackupRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescribeBackupResult describeBackup(DescribeBackupRequest describeBackupRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescribeContinuousBackupsResult describeContinuousBackups(
			DescribeContinuousBackupsRequest describeContinuousBackupsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescribeEndpointsResult describeEndpoints(DescribeEndpointsRequest describeEndpointsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescribeGlobalTableResult describeGlobalTable(DescribeGlobalTableRequest describeGlobalTableRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescribeGlobalTableSettingsResult describeGlobalTableSettings(
			DescribeGlobalTableSettingsRequest describeGlobalTableSettingsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListBackupsResult listBackups(ListBackupsRequest listBackupsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListGlobalTablesResult listGlobalTables(ListGlobalTablesRequest listGlobalTablesRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestoreTableFromBackupResult restoreTableFromBackup(
			RestoreTableFromBackupRequest restoreTableFromBackupRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestoreTableToPointInTimeResult restoreTableToPointInTime(
			RestoreTableToPointInTimeRequest restoreTableToPointInTimeRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateContinuousBackupsResult updateContinuousBackups(
			UpdateContinuousBackupsRequest updateContinuousBackupsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateGlobalTableResult updateGlobalTable(UpdateGlobalTableRequest updateGlobalTableRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateGlobalTableSettingsResult updateGlobalTableSettings(
			UpdateGlobalTableSettingsRequest updateGlobalTableSettingsRequest) {
		// TODO Auto-generated method stub
		return null;
	}


}
