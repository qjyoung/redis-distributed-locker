#!/usr/bin/env bash
function myLog() {
  echo -e "\033[34m $(date +"%Y-%m-%d %H:%M:%S") ======== $1 ======== \033[0m"
}

git update-index -q --ignore-submodules --refresh
if ! git diff-files --quiet --ignore-submodules --; then
  myLog "Warning: Your local changes would be overwritten, Please commit or stash them and try again."
  exit
fi

userName=$(git config --get user.name)
userEmail=$(git config --get user.email)
myLog "user.name: ${userName}"
myLog "user.email: ${userEmail}"

version=$(date +"%Y%m%d%H%M")
myLog "version=${version}"
git tag "github-${version}"

git config user.name "Harry Potter"
git config user.email "harry@potter.com"

if [ -z "$(git branch | grep github_latest)" ]; then
  myLog "checkout new branch github_latest"
  git checkout --orphan github_latest
  rm -rf patch_to_github.sh
  git add -A
  git commit -am "latest"
  git push github HEAD:master --force
else
  myLog "patch start..."
  myLog "changes between the branches: $(git diff master github_latest --stat)\e"

  git checkout github_latest
  git diff github_latest master >>tmp.patch
  git apply tmp.patch
  rm -rf tmp.patch patch_to_github.sh

  git add .
  git commit -m "latest"
  git push github HEAD:master
  myLog "patched success"
fi

myLog "revert user info"
git config user.name "${userName}"
git config user.email "${userEmail}"

myLog "user.name: $(git config --get user.name)"
myLog "user.email: $(git config --get user.email)"

git checkout master
