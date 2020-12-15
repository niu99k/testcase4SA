package org.gg.sa.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gg.entity.*;

import java.io.*;
import java.text.DateFormat;
import java.util.*;

public abstract class BaseTestCase4SimpleMachine implements LoadFile {
    private Machine machine;
    private List<Job> jobList;

    @Override
    public String toString() {
        return "BaseTestCase4SimpleMachine{" +
                "machine=" + machine +
                ", jobList=" + jobList +
                '}';
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public void init(String jsonName) {
        String path = BaseTestCase4SimpleMachine.class.getClassLoader().getResource("testcase1.json").getPath();
        String s = readJsonFile(path);
        JSONObject jsonObject = JSON.parseObject(s);

        this.machine = machine((JSONObject) jsonObject.get("machine"));
        this.jobList = jobList((JSONArray) jsonObject.get("jobs"));
    }

    public Machine machine(JSONObject machine) {
        Machine result = new Machine();
        result.setId((Integer) machine.get("machineId"));
        JSONArray taktMapJson = (JSONArray) machine.get("takt");

        Map<Integer, Double> taktMap = new HashMap<>();
        taktMapJson.forEach(taktInfo -> taktMap.put(((JSONObject) taktInfo).getInteger("jobId"), ((JSONObject) taktInfo).getDouble("takt")));
        result.setTakt(taktMap);
        return result;
    }

    public List<Job> jobList(JSONArray jobs) {
        List<Job> result = new ArrayList<>();
        Map<Integer, List<Integer>> preMap = new HashMap<>();
        Map<Integer, List<Integer>> postMap = new HashMap<>();
        Map<Integer, Job> jobMap = new HashMap<>();
        jobs.forEach(jobInfo -> {
                    Job job = new Job();
                    final JSONObject jobInfoJSONObject = (JSONObject) jobInfo;
                    job.setId(jobInfoJSONObject.getInteger("jobId"));
                    jobMap.put(job.getId(), job);
                    if (jobInfoJSONObject.containsKey("pre")) {
                        preMap.put(job.getId(), jobInfoJSONObject.getJSONArray("pre").toJavaList(Integer.class));
                    }
                    if (jobInfoJSONObject.containsKey("next")) {
                        postMap.put(job.getId(), jobInfoJSONObject.getJSONArray("next").toJavaList(Integer.class));
                    }
                    result.add(job);
                }
        );
        result.forEach(job -> {
            List<Job> pre = new ArrayList<>();
            List<Job> next = new ArrayList<>();
            if (preMap.containsKey(job.getId())) {
                preMap.get(job.getId()).forEach(preJobId -> pre.add(jobMap.get(preJobId)));
            }
            if (postMap.containsKey(job.getId())) {
                postMap.get(job.getId()).forEach(nextJobId -> next.add(jobMap.get(nextJobId)));
            }
            job.setPrecPre(pre);
            job.setPrecNext(next);
        });
        return result;
    }

}
