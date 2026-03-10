terraform {
  backend "s3" {
    bucket  = "nntan04129-tf-state"
    key     = "terraform/web-app.tfstate"
    region  = "us-east-1"
    encrypt = true
    # dynamodb_table = "terraform-state-locking"
  }
}