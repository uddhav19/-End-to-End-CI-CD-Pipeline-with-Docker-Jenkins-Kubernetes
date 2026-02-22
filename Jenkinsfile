pipeline { 
    agent any 
     
    environment {  
        DOCKERHUB_USERNAME = "uddhav19"  
        IMAGE_NAME = "my-app"  
        IMAGE_TAG = "${BUILD_NUMBER}"  
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-cred')  
    }  
 
    stages { 
        stage('checkout code') { 
            steps { 
                git 'https://github.com/uddhav19/myweb_cicd_project.git' 
            } 
        } 
        stage('maven build'){ 
            steps { 
                sh 'mvn clean package' 
            } 
        } 
        stage('docker image build'){ 
            steps { 
                sh 'docker build -t ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG} .' 
            } 
        } 
         stage('dockerhub login'){  
            steps {  
                sh 'echo "${DOCKERHUB_CREDENTIALS_PSW}" | docker login -u 
"${DOCKERHUB_CREDENTIALS_USR}" --password-stdin'  
            }  
        } 
         stage('push image to dockerhub'){  
            steps {  
                sh 'docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}'  
            }  
        } 
        stage('Deployment image to new image') { 
            steps { 
                sh "sed -i 's|image: .*|image: 
${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}|g' deployments.yml" 
            } 
        } 
        stage('k8s depoyment') { 
            steps { 
                sh 'kubectl apply -f k8s/deployments.yaml'
                sh 'kubectl apply -f k8s/service.yml'
            } 
        } 
    } 
}
