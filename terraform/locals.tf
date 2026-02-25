locals {
  ssm_env_path = "/bakery/${var.env}"

  env_from_ssm = zipmap(
    [for n in data.aws_ssm_parameters_by_path.app_env.names : basename(n)],
    data.aws_ssm_parameters_by_path.app_env.values
  )

  # allow overriding/adding from var.env
  env_docker_all = merge(local.env_from_ssm, var.env_docker)
}