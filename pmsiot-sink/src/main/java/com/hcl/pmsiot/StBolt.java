package com.hcl.pmsiot;

import java.util.Map;

import org.apache.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class StBolt implements IBasicBolt {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(StBolt.class);

	private static Session session = null;
	private Cluster cluster = null;
	JSONObject eventJson = null;
	String topicname = null;


	com.datastax.driver.core.ResultSet segmentlistResult = null;
	com.datastax.driver.core.ResultSet newCountUpdatedResult = null;
	
	public StBolt(String topicname) {
		this.topicname = topicname;
	}

	public void prepare(Map stormConf, TopologyContext topologyContext) {
		System.out.println("creating connection with cassandra");
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();

		try {
			session = cluster.connect();
		} catch (Exception e) {
			System.out.println("Cassandra connection error");
			e.printStackTrace();
		}

		System.out.println("connected to Cassandra");

	}

	public void execute(Tuple input, BasicOutputCollector collector) {

		Fields fields = input.getFields();

		try {
			eventJson = (JSONObject) JSONSerializer.toJSON((String) input.getValueByField(fields.get(0)));
			topicname = (String) eventJson.get("topicname");
			String ip = (String) eventJson.get("ip");
			String menu = (String) eventJson.get("menu");
			String product  = (String) eventJson.get("product");
			String count  = (String) eventJson.get("count");
			

			String qry = " INSERT INTO webapp.viewcount (topicname, ip, menu, product, count) "
					+ "VALUES ('" + topicname + "', '"+ip+"', '"+menu+"', '"+product+"', '"+count+"')";
			System.out.println("query = "+qry);
			
			segmentlistResult = session.execute(qry);
			System.out.println("query executed");
			System.out.println(segmentlistResult.getExecutionInfo());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	public void cleanup() {

	}

}
