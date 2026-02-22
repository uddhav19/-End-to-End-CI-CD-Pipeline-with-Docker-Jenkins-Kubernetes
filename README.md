# End-to-End CI/CD Pipeline with Docker, Jenkins & Kubernetes

[![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-red?logo=jenkins)](https://www.jenkins.io/)
[![Docker](https://img.shields.io/badge/Docker-Container-blue?logo=docker)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-EKS-326CE5?logo=kubernetes)](https://kubernetes.io/)
[![AWS](https://img.shields.io/badge/AWS-EKS-FF9900?logo=amazonaws)](https://aws.amazon.com/eks/)

> **Author:** Uddhav Hon 

A fully automated CI/CD pipeline that builds a Java/Maven web application, containerizes it with Docker, pushes the image to DockerHub, and deploys it to an AWS EKS (Kubernetes) cluster — triggered automatically on every GitHub commit.

---

## Architecture Overview

```
Developer → GitHub Push
                │
                ▼
          Jenkins (EC2)
          ├── Pull Code (Git)
          ├── Build Artifact (Maven)
          ├── Build Docker Image
          ├── Push to DockerHub
          └── Deploy to EKS (kubectl)
                │
                ▼
         AWS EKS Cluster
         ├── Worker Node 1
         └── Worker Node 2
                │
                ▼
     Application (NodePort Service)
     http://<NodeIP>:<Port>/<app-name>
```

---

## Prerequisites

- AWS account with appropriate permissions
- GitHub account with your application repository
- DockerHub account
- Basic knowledge of Linux, Docker, and Kubernetes

---

## Setup Guide

### Step 1 — Launch Jenkins EC2 Instance

Launch an EC2 instance (t3.medium recommended) on AWS to serve as the Jenkins server. Ensure the security group allows inbound traffic on port **8080** (Jenkins UI) and **22** (SSH).

### Step 2 — Install Required Packages on Jenkins Server

SSH into the instance and install all dependencies:

```bash
# Java (required for Jenkins)
sudo apt install openjdk-17-jdk -y

# Jenkins
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update && sudo apt install jenkins -y

# Git
sudo apt install git -y

# Docker
sudo apt install docker.io -y

# Maven
sudo apt install maven -y

# kubectl
curl -LO "https://dl.k8s.io/release/$(curl -Ls https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo cp kubectl /usr/bin/
```

### Step 3 — Access Jenkins UI

Navigate to `http://<EC2-Public-IP>:8080` and unlock Jenkins using the initial admin password:

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

Complete the setup wizard and install suggested plugins.

---

### Step 4 — Create IAM Roles for EKS

#### a) EKS Cluster Role (`masternode-role`)
- **Trusted entity:** AWS Service → EKS → EKS - Cluster
- **Policy:** `AmazonEKSClusterPolicy`

#### b) EKS Node Group Role (`worker-node-role`)
- **Trusted entity:** AWS Service → EC2
- **Policies:**
  - `AmazonEKSWorkerNodePolicy`
  - `AmazonEKS_CNI_Policy`
  - `AmazonEC2ContainerRegistryReadOnly`

---

### Step 5 — Create EKS Cluster

1. Go to **AWS Console → EKS → Create Cluster**
2. Cluster name: `eks-cluster`
3. Cluster IAM Role: `masternode-role`
4. Kubernetes version: `1.33`
5. Networking: Select your VPC, subnets, and set endpoint access to **Public and private**
6. Wait for cluster status to become **Active** (~10–15 minutes)

### Step 6 — Create EKS Node Group

1. Navigate to your cluster → **Compute → Add Node Group**
2. Node group name: `eks-node-group`
3. Node IAM Role: `worker-node-role`
4. AMI: Amazon Linux 2023
5. Instance type: `t3.medium`
6. Scaling configuration:
   - Desired: `2`, Minimum: `2`, Maximum: `8`
7. Disk size: `20 GB`

---

### Step 7 — Configure AWS CLI & kubectl on Jenkins Server

```bash
# Install AWS CLI
sudo apt install awscli -y

# Create an IAM Access Key from AWS Console → IAM → Users → Security credentials

# Configure AWS CLI as jenkins user
su - jenkins
aws configure
# Enter: Access Key ID, Secret Access Key, Region (us-east-1), Output format (yaml)

# Connect kubectl to EKS cluster
aws eks update-kubeconfig --region us-east-1 --name eks-cluster

# Verify nodes are ready
kubectl get nodes
```

---

### Step 8 — Configure Jenkins

#### Add DockerHub Credentials
1. Go to **Jenkins → Manage Jenkins → Credentials → Global**
2. Add credentials:
   - Kind: Username with password
   - Username: `<your-dockerhub-username>`
   - Password: `<your-dockerhub-password>`
   - ID: `dockerhub-cred`

#### Add Jenkins User to Docker Group
```bash
sudo usermod -aG docker jenkins
sudo systemctl restart docker
sudo systemctl restart jenkins
```

---

### Step 9 — Create Jenkins Pipeline

Create a new **Pipeline** project and set the **Pipeline Definition** to:
- **Definition:** Pipeline script from SCM
- **SCM:** Git
- **Repository URL:** `https://github.com/uddhav19/myweb_cicd_project.git`
- **Script Path:** `Jenkinsfile`

This will automatically pick up the `Jenkinsfile` from the root of your GitHub repository.

---

### Step 10— edit security groups
 edit the **security group of your EKS worker nodes** to allow inbound traffic on the assigned NodePort (e.g., 32621).

---

### Step 11 — Access the Application

```
http://<Worker-Node-Public-IP>:<NodePort>/<app-name>
```

Example: `http://54.226.163.97:32621/myweb`

---

### Step 12 — Enable Auto-Trigger on GitHub Push

In your Jenkins pipeline project, go to **Configure → Build Triggers** and enable:

- ✅ **Poll SCM** — Schedule: `* * * * *` (polls every minute)

> **Tip:** For a more efficient approach, configure a **GitHub Webhook** pointing to `http://<Jenkins-IP>:8080/github-webhook/` to trigger builds instantly on push instead of polling.

---



---

## Project Structure

```
myweb_cicd_project/
├── src/                    # Java source code
├── pom.xml                 # Maven build configuration
├── Dockerfile              # Docker image definition
├── Jenkinsfile             # Jenkins pipeline definition (Pipeline as Code)
├── deployments.yml         # Kubernetes Deployment manifest
└── service.yaml            # Kubernetes Service manifest (NodePort)
```

---

## Technologies Used

| Tool | Purpose |
|---|---|
| AWS EC2 | Jenkins server hosting |
| AWS IAM | permissions to manage AWS resources |
| AWS EKS | Managed Kubernetes cluster |
| Jenkins | CI/CD automation |
| Maven | Java build tool |
| Docker | Application containerization |
| DockerHub | Container image registry |
| kubectl | Kubernetes CLI |
| GitHub | Source code management |
