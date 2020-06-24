package sputnik;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static boolean sortuta = false;

    public static void main(String[] args) throws IOException, ApiException {

        String name = "deployberria";
        String imagePath = "gcr.io/clusterekaitz/osagaiberriak:deployBerria";
        int port = 6000;

        V1Deployment newDeploy = null;
        // Oraingoz bat bakarrik
        if (!sortuta)
            newDeploy = createDeploy(name, imagePath, port);
        if (newDeploy != null) {
            sortuta = true;
            System.out.println("Deployment berria sortuta");
            System.out.println("-----------------------");
            System.out.println("\tKind: " + newDeploy.getKind());
            System.out.println("\tName: " + newDeploy.getMetadata().getName());
            System.out.println("\tStatus: " + newDeploy.getStatus());
            System.out.println("\tID: " + newDeploy.getMetadata().getUid());
            System.out.println("\tCluster name: " + newDeploy.getMetadata().getClusterName());
            System.out.println("\tNamespace: " + newDeploy.getMetadata().getNamespace());
            System.out.println("\tReplicas: " + newDeploy.getSpec().getReplicas());
            System.out.println("\tNode: " + newDeploy.getSpec().getTemplate().getSpec().getNodeName());
            System.out.println("\tScheduler: " + newDeploy.getSpec().getTemplate().getSpec().getSchedulerName());
            System.out.println("\tImage: " + newDeploy.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
            System.out.println("\tRestart policy: " + newDeploy.getSpec().getTemplate().getSpec().getRestartPolicy());
            System.out.println("-----------------------");
        }
    }

    public static V1Deployment createDeploy(String name, String imagePath, int portNumber) throws IOException, ApiException {

        String kubeConfigPath = "~/.kube/config";
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		Configuration.setDefaultApiClient(client);

        AppsV1Api api = new AppsV1Api();

        // Lehenik Deploy-aren metadata osatuko da
        V1ObjectMeta metadata = new V1ObjectMeta();
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "sortuberria");
        metadata.setLabels(labels);
        metadata.setName(name);

        // Template zatirako metadata
        V1ObjectMeta templateMetadata = new V1ObjectMeta();
        templateMetadata.setLabels(labels);

        // Template zatirako spec
        V1PodSpec templateSpec = new V1PodSpec();
        // Container creation
        V1Container container = new V1Container();
        container.setImage(imagePath);
        container.setName(name);
        // Ports
        V1ContainerPort port = new V1ContainerPort();
        port.setContainerPort(portNumber);
        List<V1ContainerPort> allPorts = new ArrayList<>();
        container.setPorts(allPorts);
        List<V1Container> allContainers = new ArrayList<>();
        allContainers.add(container);

        templateSpec.setContainers(allContainers);
        templateSpec.setRestartPolicy("Always");

        // Spec zatiaren selector
        V1LabelSelector selector = new V1LabelSelector();
        Map<String, String> matchLabels = new HashMap<>();
        matchLabels.put("app", "sortuberria");
        selector.setMatchLabels(matchLabels);

        // Spec zatiaren template
        V1PodTemplateSpec template = new V1PodTemplateSpec();
        template.setMetadata(templateMetadata);
        template.setSpec(templateSpec);

        // Spec zatia
        V1DeploymentSpec spec = new V1DeploymentSpec();
        spec.setReplicas(1);
        spec.setSelector(selector);
        spec.setTemplate(template);

        V1Deployment newDeployBody = new V1Deployment();
        newDeployBody.setApiVersion("apps/v1");
        newDeployBody.setKind("Deployment");
        newDeployBody.setMetadata(metadata);
        newDeployBody.setSpec(spec);

        return  api.createNamespacedDeployment("default", newDeployBody, Boolean.FALSE, null, null);
    }
}
