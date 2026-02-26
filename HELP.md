### Git Work Flow
Branches:
* Feature
* Develop
* Staging
* Production

Work flow:
* When working, create feature branch and merge to develop
* To deploy to server staging, merge from develop to staging
* When release, merge from staging to production and create new release tag

### Build and push image

* docker build --platform linux/amd64 -t nntan041299/bakery-user-service:latest .
* docker tag nntan041299/bakery-user-service:latest nntan041299/bakery-user-service:latest
* docker push nntan041299/bakery-user-service:latest


### Terraform
* cd terraform
* terraform init
* terraform plan
* terraform apply -auto-approve
* terraform destroy -auto-approve