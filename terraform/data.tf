############################
# Networking (default VPC)
############################
data "aws_vpc" "default_vpc" {
  default = true
}

data "aws_subnet_ids" "default_subnet" {
  vpc_id = data.aws_vpc.default_vpc.id
}

############################
# SSM -> env map
############################
data "aws_ssm_parameters_by_path" "app_env" {
  path            = var.ssm_env_path
  recursive       = true
  with_decryption = true
}