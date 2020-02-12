@Library('jenkins-shared-lib')_

pipeline {
    agent { label 'ci' }
    options { 
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '5')) 
        }
    environment{
        APPLCICATION   = 'hello-world'
        ARTIFACT_ZIP   = misc.artifactZip(APPLCICATION)
        S3_BUCKET      = misc.artifactBucket(APPLCICATION)
    }
    stages {
        stage('Build') {
            steps {
                maven args: "clean install -U"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    script {
                        misc.packageArtifact(ARTIFACT_ZIP, "target/*.war")
                        awss3cp s3_object: ARTIFACT_ZIP,destination: misc.s3BucketPadding(S3_BUCKET)   
                        archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                    }
                }
                failure {
                    println "Slack notification: Build stage failure"
                }
            }
        }
        stage('Integration Test') {
            when {
                branch 'master'
            }
            steps {
                println 'Integration testing'
            }
        }
    }
}