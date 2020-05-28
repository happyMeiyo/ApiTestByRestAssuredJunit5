pipeline {
	agent any
	stages {
		stage('Preparation') {
			steps {
				//Wait 2 minutes
				sleep(time: 2, unit: 'MINUTES')
				// Get some code from a GitHub repository
				git(url: 'git@gitlab.caibaopay.com:dengmingyao/ApiTestForKaimaiCashier.git', branch: 'master', changelog: true, credentialsId: '6a944868-6bf7-445e-aa78-94b233e043e4')
			}
		}

		stage('Test') {
			steps {
				sh 'mvn test'
			}
		}
	}

	post {
		always {
			echo 'allure report'
			allure jdk: 'JDK8', results: [[path: 'target/allure-results']]
		}
	}
}
