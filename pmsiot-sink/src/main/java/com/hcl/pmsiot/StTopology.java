package com.hcl.pmsiot;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;


public class StTopology {

	public static void main(String[] args) throws Exception {
		if (args.length == 4) {
			
			BrokerHosts hosts = new ZkHosts("localhost:2181");

			SpoutConfig kafkaConf1 = new SpoutConfig(hosts, args[1], args[2],
					args[3]);

			kafkaConf1.forceFromStart = false;
			kafkaConf1.zkRoot = args[2];
			kafkaConf1.scheme = new SchemeAsMultiScheme(new StringScheme());
			KafkaSpout kafkaSpout1 = new KafkaSpout(kafkaConf1);
			
			StBolt countbolt = new StBolt(args[1]);
			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("kafkaspout", kafkaSpout1, 1);
			builder.setBolt("counterbolt", countbolt, 1).shuffleGrouping(
					"kafkaspout");
			Config config = new Config();
			config.setDebug(true);
			config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 1);
			config.setNumWorkers(1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(args[0], config, builder.createTopology());

			// StormSubmitter.submitTopology(args[0], config,
			// builder.createTopology());

		} else {
			System.out
					.println("Insufficent Arguements - topologyName kafkaTopic ZKRoot ID");
		}
	}
}
